package main

import (
	"fmt"
	"github.com/opentracing/opentracing-go"
	"github.com/uber/jaeger-client-go"
	"github.com/uber/jaeger-client-go/config"
	"io"
	"time"
)

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
		ServiceName: "HelloWorld",
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
