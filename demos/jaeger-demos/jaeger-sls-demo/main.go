package main

import (
	"flag"
	"fmt"
	"github.com/opentracing/opentracing-go"
	"github.com/uber/jaeger-client-go"
	"github.com/uber/jaeger-client-go/config"
	"io"
	"os"
	"time"
)

var (
	project         string
	instanceId      string
	accessKeyId     string
	accessKeySecret string
	endPoint        string
	service         string
)

func init() {
	flag.StringVar(&project, "project", os.Getenv("PROJECT"), "the project name")
	flag.StringVar(&instanceId, "instanceID", os.Getenv("INSTANCE"), "the instance name")
	flag.StringVar(&accessKeyId, "access-key-id", os.Getenv("ACCESS_KEY_ID"), "the access key id")
	flag.StringVar(&accessKeySecret, "access-key-secret", os.Getenv("ACCESS_KEY_SECRET"), "the access key secret")
	flag.StringVar(&endPoint, "endPoint", os.Getenv("ENDPOINT"), "the endpoint")
	flag.StringVar(&service, "service", os.Getenv("SERVICE_NAME"), "the service name")
}

func main() {
	tracer, closer := initJaeger()
	defer closer.Close()

	span := tracer.StartSpan("test")
	span.SetTag("test", "test")
	span.Finish()

	time.Sleep(time.Second * 30)
}

func initJaeger() (opentracing.Tracer, io.Closer) {
	cfg := &config.Configuration{
		ServiceName: service,
		Tags: []opentracing.Tag{
			{Key: "sls.otel.project", Value: project},
			{Key: "sls.otel.instanceid", Value: instanceId},
			{Key: "sls.otel.akid", Value: accessKeyId},
			{Key: "sls.otel.aksecret", Value: accessKeySecret},
		},
		Reporter: &config.ReporterConfig{
			CollectorEndpoint: endPoint,
		},
		Sampler: &config.SamplerConfig{
			Type:  "const",
			Param: 1,
		},
	}

	tracer, closer, err := cfg.NewTracer(config.Logger(jaeger.StdLogger))
	if err != nil {
		panic(fmt.Sprintf("Error: Failed to init Jaeger"))
	}

	return tracer, closer
}
