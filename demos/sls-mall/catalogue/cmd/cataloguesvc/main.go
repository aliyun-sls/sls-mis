package main

import (
	"flag"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"path/filepath"
	"syscall"

	"github.com/go-kit/kit/log"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
	"github.com/prometheus/client_golang/prometheus"
	stdprometheus "github.com/prometheus/client_golang/prometheus"
	"github.com/sls-microservices-demo/catalogue"
	"github.com/weaveworks/common/middleware"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/otlp"
	"go.opentelemetry.io/otel/exporters/stdout"
	"go.opentelemetry.io/otel/propagation"
	export "go.opentelemetry.io/otel/sdk/export/trace"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	"go.opentelemetry.io/otel/semconv"
	"golang.org/x/net/context"
)

const (
	ServiceName = "catalogue"
)

var (
	HTTPLatency = stdprometheus.NewHistogramVec(stdprometheus.HistogramOpts{
		Name:    "http_request_duration_seconds",
		Help:    "Time (in seconds) spent serving HTTP requests.",
		Buckets: stdprometheus.DefBuckets,
	}, []string{"method", "path", "status_code", "isWS"})
	ResponseBodySize = stdprometheus.NewHistogramVec(stdprometheus.HistogramOpts{
		Name:    "http_response_size",
		Buckets: stdprometheus.DefBuckets,
	}, []string{"method", "path"})
	RequestBodySize = stdprometheus.NewHistogramVec(stdprometheus.HistogramOpts{
		Name:    "http_request_size",
		Buckets: stdprometheus.DefBuckets,
	}, []string{"method", "path"})
	InflightRequests = stdprometheus.NewGaugeVec(stdprometheus.GaugeOpts{
		Name: "http_inflight_requests",
	}, []string{"method", "path"})
)

// Log domain.
var logger log.Logger
var otlpEndpoint string

func init() {
	flag.StringVar(&otlpEndpoint, "otlp-endpoint", os.Getenv("OTLP_ENDPOINT"), "otlp endpoint")
}

func initTracer() {

	var traceExporter export.SpanExporter

	if otlpEndpoint == "stdout" {
		// Create stdout exporter to be able to retrieve
		// the collected spans.
		exporter, err := stdout.NewExporter()
		if err != nil {
			panic(err)
		}
		logger.Log("register stdout exporter", "")
		traceExporter = exporter
	} else if otlpEndpoint != "" {
		// If the OpenTelemetry Collector is running on a local cluster (minikube or
		// microk8s), it should be accessible through the NodePort service at the
		// `localhost:30080` address. Otherwise, replace `localhost` with the
		// address of your cluster. If you run the app inside k8s, then you can
		// probably connect directly to the service through dns
		exp, err := otlp.NewExporter(context.Background(),
			otlp.WithInsecure(),
			otlp.WithAddress(otlpEndpoint),
			//otlp.WithGRPCDialOption(grpc.WithBlock()), // useful for testing
		)
		if err != nil {
			panic(err)
		}
		logger.Log("register otlp exporter", otlpEndpoint)
		traceExporter = exp
	}
	if traceExporter == nil {
		logger.Log("no opentelemetry exporter", "")
		return
	}

	hostname, _ := os.Hostname()
	// For the demonstration, use sdktrace.AlwaysSample sampler to sample all traces.
	// In a production application, use sdktrace.ProbabilitySampler with a desired probability.
	tp := sdktrace.NewTracerProvider(sdktrace.WithConfig(sdktrace.Config{DefaultSampler: sdktrace.AlwaysSample()}),
		sdktrace.WithSyncer(traceExporter),
		//sdktrace.WithSyncer(&TTE{}),
		sdktrace.WithResource(resource.NewWithAttributes(semconv.ServiceNameKey.String("catalogue"), semconv.HostNameKey.String(hostname))))
	otel.SetTracerProvider(tp)
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(propagation.TraceContext{}, propagation.Baggage{}))
}

func init() {
	prometheus.MustRegister(HTTPLatency)
	prometheus.MustRegister(ResponseBodySize)
	prometheus.MustRegister(RequestBodySize)
	prometheus.MustRegister(InflightRequests)
}

func main() {
	var (
		port   = flag.String("port", "80", "Port to bind HTTP listener") // TODO(pb): should be -addr, default ":80"
		images = flag.String("images", "./images/", "Image path")
		dsn    = flag.String("DSN", "catalogue_user:default_password@tcp(catalogue-db:3306)/socksdb", "Data Source Name: [username[:password]@][protocol[(address)]]/dbname")
	)
	flag.Parse()
	fmt.Fprintf(os.Stderr, "images: %q\n", *images)
	abs, err := filepath.Abs(*images)
	fmt.Fprintf(os.Stderr, "Abs(images): %q (%v)\n", abs, err)
	pwd, err := os.Getwd()
	fmt.Fprintf(os.Stderr, "Getwd: %q (%v)\n", pwd, err)
	files, _ := filepath.Glob(*images + "/*")
	fmt.Fprintf(os.Stderr, "ls: %q\n", files) // contains a list of all files in the current directory

	// Mechanical stuff.
	errc := make(chan error)
	ctx := context.Background()

	{
		logger = log.NewLogfmtLogger(os.Stderr)
		logger = log.With(logger, "ts", log.DefaultTimestampUTC)
		logger = log.With(logger, "caller", log.DefaultCaller)
		initTracer()
	}

	// Data domain.
	db, err := sqlx.Open("mysql", *dsn)
	if err != nil {
		logger.Log("err", err)
		os.Exit(1)
	}
	defer db.Close()

	// Check if DB connection can be made, only for logging purposes, should not fail/exit
	err = db.Ping()
	if err != nil {
		logger.Log("Error", "Unable to connect to Database", "DSN", dsn)
	}

	// Service domain.
	var service catalogue.Service
	{
		service = catalogue.NewCatalogueService(db, logger)
		service = catalogue.LoggingMiddleware(logger)(service)
	}

	// Endpoint domain.
	endpoints := catalogue.MakeEndpoints(service)

	// HTTP router
	router := catalogue.MakeHTTPHandler(ctx, endpoints, *images, logger)

	httpMiddleware := []middleware.Interface{
		middleware.Instrument{
			Duration:         HTTPLatency,
			RequestBodySize:  RequestBodySize,
			ResponseBodySize: ResponseBodySize,
			InflightRequests: InflightRequests,
			RouteMatcher:     router,
		},
	}

	// Handler
	handler := middleware.Merge(httpMiddleware...).Wrap(router)

	// Create and launch the HTTP server.
	go func() {
		logger.Log("transport", "HTTP", "port", *port)
		errc <- http.ListenAndServe(":"+*port, handler)
	}()

	// Capture interrupts.
	go func() {
		c := make(chan os.Signal)
		signal.Notify(c, syscall.SIGINT, syscall.SIGTERM)
		errc <- fmt.Errorf("%s", <-c)
	}()

	logger.Log("exit", <-errc)
}
