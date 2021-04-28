module github.com/alibaba-sls/opentelemetry-go-demo

go 1.16

require (
	github.com/aliyun-sls/opentelemetry-go-provider-sls v0.1.0
	github.com/gorilla/mux v1.8.0
	github.com/jessevdk/go-flags v1.5.0
	go.opentelemetry.io/contrib/instrumentation/host v0.16.0
	go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp v0.16.0 // indirect
	go.opentelemetry.io/contrib/instrumentation/runtime v0.16.0
	go.opentelemetry.io/otel v0.16.0
	go.opentelemetry.io/otel/exporters/otlp v0.16.0
	go.opentelemetry.io/otel/exporters/stdout v0.16.0
	go.opentelemetry.io/otel/sdk v0.16.0
)
