package main

import (
	"flag"
	"fmt"
	"github.com/aliyun-sls/opentelemetry-go-provider-sls/provider"
	corelog "log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-kit/kit/log"
	"github.com/sls-mis/demos/sls-mall/user/api"
	"github.com/sls-mis/demos/sls-mall/user/db"
	"github.com/sls-mis/demos/sls-mall/user/db/mongodb"
)

var (
	port           string
	project        string
	instance       string
	accessKeyId    string
	endpoint       string
	serviceName    string
	serviceVersion string
	accessSecret   string
)

const (
	ServiceName = "user"
)

func init() {
	flag.StringVar(&project, "project", os.Getenv("PROJECT"), "Zipkin address")
	flag.StringVar(&instance, "instance", os.Getenv("INSTANCE"), "Zipkin address")
	flag.StringVar(&accessKeyId, "accessKeyId", os.Getenv("ACCESS_KEY_ID"), "Zipkin address")
	flag.StringVar(&accessSecret, "accessSecret", os.Getenv("ACCESS_SECRET"), "Zipkin address")
	flag.StringVar(&endpoint, "endpoint", os.Getenv("ENDPOINT"), "Zipkin address")
	flag.StringVar(&serviceName, "serviceName", os.Getenv("SERVICE_NAME"), "Zipkin address")
	flag.StringVar(&serviceVersion, "serviceVersion", os.Getenv("SERVICE_VERSION"), "Zipkin address")
	flag.StringVar(&port, "port", "8084", "Port on which to run")
	db.Register("mongodb", &mongodb.Mongo{})
}

func main() {

	flag.Parse()
	// Mechanical stuff.
	errc := make(chan error)

	// Log domain.
	var logger log.Logger
	{
		logger = log.NewLogfmtLogger(os.Stderr)
		logger = log.With(logger, "ts", log.DefaultTimestampUTC)
		logger = log.With(logger, "caller", log.DefaultCaller)
	}

	conn, err := net.Dial("udp", "8.8.8.8:80")
	if err != nil {
		logger.Log("err", err)
		os.Exit(1)
	}
	defer conn.Close()

	slsConfig, err := provider.NewConfig(provider.WithServiceName(serviceName),
		provider.WithServiceVersion(serviceVersion),
		provider.WithTraceExporterEndpoint(endpoint),
		provider.WithSLSConfig(project, instance, accessKeyId, accessSecret))
	if err != nil {
		panic(err)
	}
	if err := provider.Start(slsConfig); err != nil {
		panic(err)
	}

	dbconn := false
	for !dbconn {
		err := db.Init()
		if err != nil {
			if err == db.ErrNoDatabaseSelected {
				corelog.Fatal(err)
			}
			corelog.Print(err)
		} else {
			dbconn = true
		}
	}

	var service api.Service
	{
		service = api.NewFixedService()
		service = api.LoggingMiddleware(logger)(service)
	}

	endpoints := api.MakeEndpoints(service, logger)
	router := api.MakeHTTPHandler(endpoints, logger)
	srv := &http.Server{
		Handler:      router,
		Addr:         "0.0.0.0:8084",
		WriteTimeout: 15 * time.Second,
		ReadTimeout:  15 * time.Second,
	}

	go func() {
		logger.Log("transport", "HTTP", "port", port)
		errc <- srv.ListenAndServe()
	}()

	go func() {
		c := make(chan os.Signal)
		signal.Notify(c, syscall.SIGINT, syscall.SIGTERM)
		errc <- fmt.Errorf("%s", <-c)
	}()
	logger.Log("exit", <-errc)
}
