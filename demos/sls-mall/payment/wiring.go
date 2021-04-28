package payment

import (
	"net/http"
	"os"

	"github.com/go-kit/kit/log"
	"golang.org/x/net/context"

	"github.com/prometheus/client_golang/prometheus"
	stdprometheus "github.com/prometheus/client_golang/prometheus"
	"github.com/weaveworks/common/middleware"
)

var (
	HTTPLatency = stdprometheus.NewHistogramVec(stdprometheus.HistogramOpts{
		Name:    "http_request_duration_seconds",
		Help:    "Time (in seconds) spent serving HTTP requests.",
		Buckets: stdprometheus.DefBuckets,
	}, []string{"method", "path", "status_code", "isWS"})
	ResponseBodySize = stdprometheus.NewHistogramVec(stdprometheus.HistogramOpts{
		Name:    "http_response_size",
		Buckets: stdprometheus.DefBuckets,
	}, []string{"method", "path"})
	RequestBodySize = stdprometheus.NewHistogramVec(stdprometheus.HistogramOpts{
		Name:    "http_request_size",
		Buckets: stdprometheus.DefBuckets,
	}, []string{"method", "path"})
	InflightRequests = stdprometheus.NewGaugeVec(stdprometheus.GaugeOpts{
		Name: "http_inflight_requests",
	}, []string{"method", "path"})
)

func init() {
	prometheus.MustRegister(HTTPLatency)
	prometheus.MustRegister(ResponseBodySize)
	prometheus.MustRegister(RequestBodySize)
	prometheus.MustRegister(InflightRequests)
}

func WireUp(ctx context.Context, declineAmount float32, serviceName string) (http.Handler, log.Logger) {
	// Log domain.
	var logger log.Logger
	{
		logger = log.NewLogfmtLogger(os.Stderr)
		logger = log.With(logger, "ts", log.DefaultTimestampUTC)
		logger = log.With(logger, "caller", log.DefaultCaller)
	}

	// Service domain.
	var service Service
	{
		service = NewAuthorisationService(declineAmount)
		service = LoggingMiddleware(logger)(service)
	}

	// Endpoint domain.
	endpoints := MakeEndpoints(service)

	router := MakeHTTPHandler(ctx, endpoints, logger)

	httpMiddleware := []middleware.Interface{
		middleware.Instrument{
			Duration:         HTTPLatency,
			RequestBodySize:  RequestBodySize,
			ResponseBodySize: ResponseBodySize,
			InflightRequests: InflightRequests,
			RouteMatcher:     router,
		},
	}

	// Handler
	handler := middleware.Merge(httpMiddleware...).Wrap(router)

	return handler, logger
}
