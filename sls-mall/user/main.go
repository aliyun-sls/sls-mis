package main

import (
	"context"
	"errors"
	"flag"
	"fmt"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"

	corelog "log"

	"github.com/go-kit/kit/log"
	kitprometheus "github.com/go-kit/kit/metrics/prometheus"
	stdprometheus "github.com/prometheus/client_golang/prometheus"
	"github.com/sls-microservices-demo/user/api"
	"github.com/sls-microservices-demo/user/db"
	"github.com/sls-microservices-demo/user/db/mongodb"
	commonMiddleware "github.com/weaveworks/common/middleware"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/otlp"
	"go.opentelemetry.io/otel/exporters/stdout"
	"go.opentelemetry.io/otel/propagation"
	export "go.opentelemetry.io/otel/sdk/export/trace"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	"go.opentelemetry.io/otel/semconv"
)

var (
	port         string
	zip          string
	initDB       bool
	otlpEndpoint string
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

const (
	ServiceName = "user"
)

func init() {
	stdprometheus.MustRegister(HTTPLatency)
	stdprometheus.MustRegister(RequestBodySize)
	stdprometheus.MustRegister(ResponseBodySize)
	stdprometheus.MustRegister(InflightRequests)
	flag.StringVar(&otlpEndpoint, "otlp-endpoint", os.Getenv("OTLP_ENDPOINT"), "otlp endpoint")
	flag.StringVar(&zip, "zipkin", os.Getenv("ZIPKIN"), "Zipkin address")
	flag.StringVar(&port, "port", "8084", "Port on which to run")
	flag.BoolVar(&initDB, "initdb", true, "Init db or not")
	db.Register("mongodb", &mongodb.Mongo{})
}

// Log domain.
var logger log.Logger

type TTE struct {
}

func (*TTE) ExportSpans(ctx context.Context, spanData []*export.SpanData) error {
	fmt.Println("ExportSpans")
	return errors.New("error")
}

// Shutdown notifies the exporter of a pending halt to operations. The
// exporter is expected to preform any cleanup or synchronization it
// requires while honoring all timeouts and cancellations contained in
// the passed context.
func (*TTE) Shutdown(ctx context.Context) error {
	fmt.Println("Shutdown")
	return errors.New("error")
}

func initTracer() {

	var traceExporter export.SpanExporter

	if otlpEndpoint == "stdout" {
		// Create stdout exporter to be able to retrieve
		// the collected spans.
		exporter, err := stdout.NewExporter(stdout.WithPrettyPrint())
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
		sdktrace.WithResource(resource.NewWithAttributes(semconv.ServiceNameKey.String("user"), semconv.HostNameKey.String(hostname))))
	otel.SetTracerProvider(tp)
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(propagation.TraceContext{}, propagation.Baggage{}))
}

func main() {

	flag.Parse()
	// Mechanical stuff.
	errc := make(chan error)

	{
		logger = log.NewLogfmtLogger(os.Stderr)
		logger = log.With(logger, "ts", log.DefaultTimestampUTC)
		logger = log.With(logger, "caller", log.DefaultCaller)
	}

	initTracer()

	// Find service local IP.
	conn, err := net.Dial("udp", "8.8.8.8:80")
	if err != nil {
		logger.Log("err", err)
		os.Exit(1)
	}
	//localAddr := conn.LocalAddr().(*net.UDPAddr)
	//host := strings.Split(localAddr.String(), ":")[0]
	defer conn.Close()

	dbconn := false
	for !dbconn {
		err := db.Init()
		if err != nil {
			if err == db.ErrNoDatabaseSelected {
				corelog.Fatal(err)
			}
			corelog.Print(err)
			if !initDB {
				dbconn = true
			}
		} else {
			dbconn = true
		}
	}

	fieldKeys := []string{"method"}
	// Service domain.
	var service api.Service
	{
		service = api.NewFixedService()
		service = api.LoggingMiddleware(logger)(service)
		service = api.NewInstrumentingService(
			kitprometheus.NewCounterFrom(
				stdprometheus.CounterOpts{
					Namespace: "microservices_demo",
					Subsystem: "user",
					Name:      "request_count",
					Help:      "Number of requests received.",
				},
				fieldKeys),
			kitprometheus.NewSummaryFrom(stdprometheus.SummaryOpts{
				Namespace: "microservices_demo",
				Subsystem: "user",
				Name:      "request_latency_microseconds",
				Help:      "Total duration of requests in microseconds.",
			}, fieldKeys),
			service,
		)
	}

	// Endpoint domain.
	endpoints := api.MakeEndpoints(service)

	// HTTP router
	router := api.MakeHTTPHandler(endpoints, logger)

	httpMiddleware := []commonMiddleware.Interface{
		commonMiddleware.Instrument{
			Duration:         HTTPLatency,
			RequestBodySize:  RequestBodySize,
			ResponseBodySize: ResponseBodySize,
			InflightRequests: InflightRequests,
			RouteMatcher:     router,
		},
	}

	// Handler
	handler := commonMiddleware.Merge(httpMiddleware...).Wrap(router)

	// Create and launch the HTTP server.
	go func() {
		logger.Log("transport", "HTTP", "port", port)
		errc <- http.ListenAndServe(fmt.Sprintf(":%v", port), handler)
	}()

	// Capture interrupts.
	go func() {
		c := make(chan os.Signal)
		signal.Notify(c, syscall.SIGINT, syscall.SIGTERM)
		errc <- fmt.Errorf("%s", <-c)
	}()

	logger.Log("exit", <-errc)
}
