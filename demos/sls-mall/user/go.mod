module github.com/sls-mis/demos/sls-mall/user

go 1.16

require (
	github.com/go-kit/kit v0.10.0
	github.com/gorilla/mux v1.8.0
	github.com/opentracing/opentracing-go v1.2.0
	github.com/openzipkin-contrib/zipkin-go-opentracing v0.4.5
	github.com/openzipkin/zipkin-go v0.2.2
	github.com/prometheus/client_golang v1.10.0
	github.com/weaveworks/common v0.0.0-20210506120931-f2676019da11
	gopkg.in/mgo.v2 v2.0.0-20190816093944-a6b53ec6cb22
	gopkg.in/tomb.v2 v2.0.0-20161208151619-d5d1b5820637 // indirect
)

replace github.com/sls-mis/demos/sls-mall/user => ./
