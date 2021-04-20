module github.com/sls-microservices-demo/user

go 1.15

require (
	github.com/go-kit/kit v0.10.0
	github.com/gorilla/mux v1.8.0
	github.com/opentracing/opentracing-go v1.2.0
	github.com/prometheus/client_golang v1.9.0
	github.com/weaveworks/common v0.0.0-20201201095205-47e357f4e1ba
	go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp v0.15.1
	go.opentelemetry.io/otel v0.15.0
	go.opentelemetry.io/otel/exporters/otlp v0.15.0
	go.opentelemetry.io/otel/exporters/stdout v0.15.0
	go.opentelemetry.io/otel/sdk v0.15.0
	gopkg.in/mgo.v2 v2.0.0-20190816093944-a6b53ec6cb22
)
