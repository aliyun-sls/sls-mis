package opentelemetry

import (
	"context"
	"github.com/go-kit/kit/endpoint"
	"github.com/go-kit/kit/log"
	kithttp "github.com/go-kit/kit/transport/http"
	"go.opentelemetry.io/otel"
	"net/http"
)

type HeaderCarrier http.Header

func TraceServer(operation string) endpoint.Middleware {
	return func(next endpoint.Endpoint) endpoint.Endpoint {
		return func(ctx context.Context, request interface{}) (interface{}, error) {
			ctx, span := otel.Tracer("").Start(ctx, operation)
			defer span.End()
			return next(ctx, request)
		}
	}
}

func HTTPToContext(operationName string, logger log.Logger) kithttp.RequestFunc {
	return func(ctx context.Context, req *http.Request) context.Context {
		return otel.GetTextMapPropagator().Extract(req.Context(), req.Header)
	}
}
