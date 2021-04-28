package main

import (
	"context"
	"flag"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"

	"github.com/go-kit/kit/log"
	"github.com/sls-microservices-demo/payment"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/otlp"
	"go.opentelemetry.io/otel/exporters/stdout"
	"go.opentelemetry.io/otel/propagation"
	export "go.opentelemetry.io/otel/sdk/export/trace"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	"go.opentelemetry.io/otel/semconv"
)

const (
	ServiceName = "payment"
)

// Log domain.
var logger log.Logger
var otlpEndpoint string

var (
	port          = flag.String("port", "8080", "Port to bind HTTP listener")
	zip           = flag.String("zipkin", os.Getenv("ZIPKIN"), "Zipkin address")
	declineAmount = flag.Float64("decline", 105, "Decline payments over certain amount")
)

func init() {
	flag.StringVar(&otlpEndpoint, "otlp-endpoint", os.Getenv("OTLP_ENDPOINT"), "otlp endpoint")
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
		sdktrace.WithResource(resource.NewWithAttributes(semconv.ServiceNameKey.String("payment"), semconv.HostNameKey.String(hostname))))
	otel.SetTracerProvider(tp)
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(propagation.TraceContext{}, propagation.Baggage{}))
}

func main() {

	flag.Parse()
	{
		// Log domain.
		{
			logger = log.NewLogfmtLogger(os.Stderr)
			logger = log.With(logger, "ts", log.DefaultTimestampUTC)
			logger = log.With(logger, "caller", log.DefaultCaller)
		}
		initTracer()

	}
	// Mechanical stuff.
	errc := make(chan error)
	ctx := context.Background()

	handler, logger := payment.WireUp(ctx, float32(*declineAmount), ServiceName)

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
