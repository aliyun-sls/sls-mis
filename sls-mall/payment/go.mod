module github.com/sls-microservices-demo/payment

go 1.15

require (
	github.com/go-kit/kit v0.10.0
	github.com/gorilla/mux v1.8.0
	github.com/microservices-demo/user v0.0.0-20201001130948-9184ccf85850 // indirect
	github.com/opentracing/opentracing-go v1.2.0
	github.com/prometheus/client_golang v1.9.0
	github.com/sls-microservices-demo/user v0.0.0-20201001130948-9184ccf85850
	github.com/streadway/handy v0.0.0-20200128134331-0f66f006fb2e
	github.com/weaveworks/common v0.0.0-20201201095205-47e357f4e1ba
	go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp v0.15.1
	go.opentelemetry.io/otel v0.15.0
	go.opentelemetry.io/otel/exporters/otlp v0.15.0
	go.opentelemetry.io/otel/exporters/stdout v0.15.0
	go.opentelemetry.io/otel/sdk v0.15.0
	golang.org/x/net v0.0.0-20201216054612-986b41b23924
	gopkg.in/mgo.v2 v2.0.0-20190816093944-a6b53ec6cb22 // indirect
)
