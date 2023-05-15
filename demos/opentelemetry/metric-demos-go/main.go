package main

import (
	"context"
	"os"
	"time"

	"go.opentelemetry.io/otel/exporters/otlp/otlpmetric/otlpmetricgrpc"
	m "go.opentelemetry.io/otel/metric"
	"go.opentelemetry.io/otel/metric/global"
	"go.opentelemetry.io/otel/sdk/metric"
	"go.opentelemetry.io/otel/sdk/resource"
	semconv "go.opentelemetry.io/otel/semconv/v1.18.0"
)

const (
	slsProjectHeader         = "x-sls-otel-project"
	slsInstanceIDHeader      = "x-sls-otel-instance-id"
	slsAccessKeyIDHeader     = "x-sls-otel-ak-id"
	slsAccessKeySecretHeader = "x-sls-otel-ak-secret"
)

func sendData() {
	headers := map[string]string{
		slsProjectHeader:         os.Getenv("SLS_PROJECT"),
		slsInstanceIDHeader:      os.Getenv("SLS_INSTANCE_ID"),
		slsAccessKeyIDHeader:     os.Getenv("SLS_ACCESS_KEY_ID"),
		slsAccessKeySecretHeader: os.Getenv("SLS_ACCESS_KEY_SECRET"),
	}

	var exp metric.Exporter
	exp, _ = otlpmetricgrpc.New(context.Background(),
		otlpmetricgrpc.WithEndpoint("qs-demos.cn-beijing.log.aliyuncs.com:10010"),
		otlpmetricgrpc.WithHeaders(headers))

	res := resource.NewWithAttributes(
		semconv.SchemaURL,
		semconv.ServiceName("my-service"),
		semconv.ServiceVersion("v0.1.0"),
	)

	meterProvider := metric.NewMeterProvider(
		metric.WithResource(res),
		metric.WithReader(metric.NewPeriodicReader(exp, metric.WithInterval(10*time.Second))),
	)

	global.SetMeterProvider(meterProvider)

	var counter m.Float64Counter
	var err error

	counter, err = global.Meter("test").Float64Counter("test_counter")

	if err != nil {
		panic(err)
	}

	for {
		counter.Add(context.Background(), 1.0)
		time.Sleep(5 * time.Second)
	}
}

func main() {
	go sendData()

	time.Sleep(1 * time.Hour)
}
