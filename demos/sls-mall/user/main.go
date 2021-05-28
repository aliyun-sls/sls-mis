package main

import (
	"flag"
	"fmt"
	corelog "log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-kit/kit/log"
	stdopentracing "github.com/opentracing/opentracing-go"
	zipkinot "github.com/openzipkin-contrib/zipkin-go-opentracing"
	"github.com/openzipkin/zipkin-go"
	zipkinhttp "github.com/openzipkin/zipkin-go/reporter/http"
	"github.com/sls-mis/demos/sls-mall/user/api"
	"github.com/sls-mis/demos/sls-mall/user/db"
	"github.com/sls-mis/demos/sls-mall/user/db/mongodb"
)

var (
	port string
	zip  string
)

const (
	ServiceName = "user"
)

func init() {
	flag.StringVar(&zip, "zipkin", os.Getenv("ZIPKIN"), "Zipkin address")
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

	var tracer stdopentracing.Tracer
	{
		if zip == "" {
			tracer = stdopentracing.NoopTracer{}
		} else {
			reporter := zipkinhttp.NewReporter(zip)
			endpoint, err := zipkin.NewEndpoint(ServiceName, "localhost:8084")
			if err != nil {
				logger.Log("unable to create local endpoint: %+v\n", err)
			}

			nativeTracer, err := zipkin.NewTracer(reporter, zipkin.WithLocalEndpoint(endpoint))
			if err != nil {
				logger.Log("unable to create tracer: %+v\n", err)
			}

			tracer = zipkinot.Wrap(nativeTracer)
		}
		stdopentracing.InitGlobalTracer(tracer)
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

	endpoints := api.MakeEndpoints(service, tracer)
	router := api.MakeHTTPHandler(endpoints, logger, tracer)
	srv := &http.Server{
		Handler: router,
		Addr:    "0.0.0.0:8084",
		// Good practice: enforce timeouts for servers you create!
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
