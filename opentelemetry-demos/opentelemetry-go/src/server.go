package main

import (
	"fmt"
	"io"
	"net/http"

	"github.com/aliyun-sls/opentelemetry-go-provider-sls/provider"
	"github.com/jessevdk/go-flags"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
)

const (
	endPoint = "/hello-world"
)

func main() {
	var opts struct {
		ServiceName    string `short:"s" long:"service-name" description:"service name" require:"true" default:"opentelemetry-go" env:"SERVICE_NAME"`
		ServiceVersion string `short:"v" long:"service-version" require:"true" description:"service version" default:"1.0.0" env:"SERVICE_VERSION"`

		Logstore     string `short:"l" long:"logstore" description:"logstore" require:"true" env:"LOGSTORE"`
		Project      string `short:"p" long:"project" description:"project" require:"true" env:"PROJECT"`
		AccessKey    string `short:"k" long:"access-key" description:"access key id" require:"true" env:"ACCESS_KEY_ID"`
		AccessSecret string `short:"a" long:"access-secret" description:"access secret" require:"true" env:"ACCESS_SECRET"`
		Endpoint     string `short:"e" long:"endpoint" description:"endpoint" require:"true" env:"ENDPOINT"`
	}

	flags.Parse(&opts)

	fmt.Println("RUNNING PARAMETERS: ")
	fmt.Println("- service name: ", opts.ServiceName)
	fmt.Println("- service version: ", opts.ServiceVersion)
	fmt.Println("- logstore: ", opts.Logstore)
	fmt.Println("- project: ", opts.Project)
	fmt.Println("- AccessKey: ", opts.AccessKey)
	fmt.Println("- AccessSecret: ", opts.AccessSecret)
	fmt.Println("- EndPoint: ", opts.Endpoint)

	slsConfig, err := provider.NewConfig(provider.WithServiceName(opts.ServiceName),
		provider.WithServiceVersion(opts.ServiceVersion),
		provider.WithTraceExporterEndpoint(opts.Endpoint),
		provider.WithMetricExporterEndpoint(opts.Endpoint),
		provider.WithSLSConfig(opts.Project, opts.Logstore, opts.AccessKey, opts.AccessSecret))
	if err != nil {
		panic(err)
	}
	if err := provider.Start(slsConfig); err != nil {
		panic(err)
	}
	defer provider.Shutdown(slsConfig)
	helloHandler := func(w http.ResponseWriter, req *http.Request) {
		_, _ = io.WriteString(w, "Hello, world!\n")
	}

	otelHandler := otelhttp.NewHandler(http.HandlerFunc(helloHandler), "Hello")
	http.Handle(endPoint, otelHandler)
	fmt.Println("Now listen port 8084, you can visit 127.0.0.1:8084/hello-word .")
	err = http.ListenAndServe(":8084", nil)

	if err != nil {
		panic(err)
	}
}
