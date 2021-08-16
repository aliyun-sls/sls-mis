package payment

import (
	"time"

	"github.com/go-kit/kit/log"
	"go.opentelemetry.io/otel/trace"
	"golang.org/x/net/context"
)

// LoggingMiddleware logs method calls, parameters, results, and elapsed time.
func LoggingMiddleware(logger log.Logger) Middleware {
	return func(next Service) Service {
		return loggingMiddleware{
			next:   next,
			logger: logger,
		}
	}
}

type loggingMiddleware struct {
	next   Service
	logger log.Logger
}

func (mw loggingMiddleware) Authorise(ctx context.Context, amount float32) (auth Authorisation, err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "Authorise",
			"result", auth.Authorised,
			"took", time.Since(begin).Microseconds(),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"error", err,
		)
	}(time.Now())
	return mw.next.Authorise(ctx, amount)
}

func (mw loggingMiddleware) Health(ctx context.Context) (health []Health) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "Health",
			"result", len(health),
			"took", time.Since(begin).Microseconds(),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
		)
	}(time.Now())
	return mw.next.Health(ctx)
}
