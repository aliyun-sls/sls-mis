module github.com/sls-mis/demos/sls-mall/user

go 1.16

require (
	github.com/aliyun-sls/opentelemetry-go-provider-sls v0.1.0
	github.com/go-kit/kit v0.10.0
	github.com/gorilla/mux v1.8.0
	go.opentelemetry.io/otel v0.16.0
	gopkg.in/mgo.v2 v2.0.0-20190816093944-a6b53ec6cb22
	gopkg.in/tomb.v2 v2.0.0-20161208151619-d5d1b5820637 // indirect
)

replace github.com/sls-mis/demos/sls-mall/user => ./
