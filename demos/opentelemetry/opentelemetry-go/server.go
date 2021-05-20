package main

import (
	"fmt"
	"io"
	"net/http"
	"strconv"
	"time"

	"github.com/aliyun-sls/opentelemetry-go-provider-sls/provider"
	"github.com/jessevdk/go-flags"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/otel/label"
	"go.opentelemetry.io/otel/trace"
)

func main() {
	var opts struct {
		ServiceName    string `short:"s" long:"service-name" description:"service name" require:"true" default:"opentelemetry-go" env:"SERVICE_NAME"`
		ServiceVersion string `short:"v" long:"service-version" require:"true" description:"service version" default:"1.0.0" env:"SERVICE_VERSION"`

		Instance     string `short:"i" long:"instance" description:"sls trace instance id" require:"true" env:"INSTANCE"`
		Project      string `short:"p" long:"project" description:"project" require:"true" env:"PROJECT"`
		AccessKey    string `long:"access-key" description:"access key id" require:"true" env:"ACCESS_KEY_ID"`
		AccessSecret string `long:"access-secret" description:"access secret" require:"true" env:"ACCESS_SECRET"`
		Endpoint     string `short:"e" long:"endpoint" description:"endpoint" default:"cn-hangzhou.log.aliyuncs.com:10010" require:"true" env:"ENDPOINT"`
	}

	if _, err := flags.Parse(&opts); err != nil {
		return
	}

	fmt.Println("RUNNING PARAMETERS: ")
	fmt.Println("- service name: ", opts.ServiceName)
	fmt.Println("- service version: ", opts.ServiceVersion)
	fmt.Println("- instance: ", opts.Instance)
	fmt.Println("- project: ", opts.Project)
	fmt.Println("- AccessKey: ", opts.AccessKey)
	fmt.Println("- AccessSecret: ", opts.AccessSecret)
	fmt.Println("- EndPoint: ", opts.Endpoint)

	slsConfig, err := provider.NewConfig(provider.WithServiceName(opts.ServiceName),
		provider.WithServiceVersion(opts.ServiceVersion),
		provider.WithTraceExporterEndpoint(opts.Endpoint),
		provider.WithMetricExporterEndpoint(opts.Endpoint),
		provider.WithSLSConfig(opts.Project, opts.Instance, opts.AccessKey, opts.AccessSecret))
	if err != nil {
		panic(err)
	}
	if err := provider.Start(slsConfig); err != nil {
		panic(err)
	}
	defer provider.Shutdown(slsConfig)

	saveOrderHandler := func(w http.ResponseWriter, req *http.Request) {
		span := trace.SpanFromContext(req.Context())
		orderIDStr := req.URL.Query().Get("orderID")
		orderID, err := strconv.ParseUint(orderIDStr, 10, 64)
		if err != nil {
			span.AddEvent("unknown order id, err : "+err.Error(), trace.WithAttributes(label.KeyValue{
				Key:   "orderID",
				Value: label.StringValue(orderIDStr),
			}))
			w.WriteHeader(http.StatusBadRequest)
			_, _ = io.WriteString(w, fmt.Sprintf("invalid order : %s\n", orderIDStr))

		} else {
			span.SetAttributes(label.KeyValue{
				Key:   "order",
				Value: label.Uint64Value(orderID),
			})

			_, saveSpan := span.Tracer().Start(req.Context(), "SaveOrderToDB")
			time.Sleep(time.Duration(time.Millisecond * 500))
			saveSpan.End()
			_, _ = io.WriteString(w, fmt.Sprintf("order %d saved success\n", orderID))
		}
	}

	otelHandler := otelhttp.NewHandler(http.HandlerFunc(saveOrderHandler), "SaveOrder")
	http.Handle("/save-order", otelHandler)

	helloHandler := func(w http.ResponseWriter, req *http.Request) {
		_, _ = io.WriteString(w, "Hello, world!\n")
	}

	otelHandler = otelhttp.NewHandler(http.HandlerFunc(helloHandler), "Hello")
	http.Handle("/hello-world", otelHandler)
	fmt.Println("Now listen port 8084, you can visit 127.0.0.1:8084/hello-world or 127.0.0.1:8084/save-order?orderID=123 .")
	err = http.ListenAndServe(":8084", nil)

	if err != nil {
		panic(err)
	}
}
