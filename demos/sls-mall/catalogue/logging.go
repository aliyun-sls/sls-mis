package catalogue

import (
	"go.opentelemetry.io/otel/trace"
	"golang.org/x/net/context"
	"strings"
	"time"

	"github.com/go-kit/kit/log"
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

func (mw loggingMiddleware) List(ctx context.Context, tags []string, order string, pageNum, pageSize int) (socks []Sock, err error) {
	defer func(begin time.Time) {
		spanContext := trace.SpanContextFromContext(ctx)
		mw.logger.Log(
			"method", "List",
			"tags", strings.Join(tags, ", "),
			"order", order,
			"pageNum", pageNum,
			"pageSize", pageSize,
			"result", len(socks),
			"err", err,
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Microseconds(),
		)
	}(time.Now())
	return mw.next.List(ctx, tags, order, pageNum, pageSize)
}

func (mw loggingMiddleware) Count(ctx context.Context, tags []string) (n int, err error) {
	defer func(begin time.Time) {
		spanContext := trace.SpanContextFromContext(ctx)
		mw.logger.Log(
			"method", "Count",
			"tags", strings.Join(tags, ", "),
			"result", n,
			"err", err,
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Microseconds(),
		)
	}(time.Now())
	return mw.next.Count(ctx, tags)
}

func (mw loggingMiddleware) Get(ctx context.Context, id string) (s Sock, err error) {
	defer func(begin time.Time) {
		spanContext := trace.SpanContextFromContext(ctx)
		mw.logger.Log(
			"method", "Get",
			"id", id,
			"sock", s.ID,
			"err", err,
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Microseconds(),
		)
	}(time.Now())
	return mw.next.Get(ctx, id)
}

func (mw loggingMiddleware) Tags(ctx context.Context) (tags []string, err error) {
	defer func(begin time.Time) {
		spanContext := trace.SpanContextFromContext(ctx)
		mw.logger.Log(
			"method", "Tags",
			"result", len(tags),
			"err", err,
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Microseconds(),
		)
	}(time.Now())
	return mw.next.Tags(ctx)
}

func (mw loggingMiddleware) Health(ctx context.Context) (health []Health) {
	defer func(begin time.Time) {
		spanContext := trace.SpanContextFromContext(ctx)
		mw.logger.Log(
			"method", "Health",
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"result", len(health),
			"took", time.Since(begin).Microseconds(),
		)
	}(time.Now())
	return mw.next.Health(ctx)
}
