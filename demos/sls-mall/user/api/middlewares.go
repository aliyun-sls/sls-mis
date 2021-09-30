package api

import (
	"context"
	"fmt"
	"go.opentelemetry.io/otel/trace"
	"time"

	"github.com/go-kit/kit/log"
	"github.com/sls-mis/demos/sls-mall/user/users"
)

// Middleware decorates a service.
type Middleware func(Service) Service

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

func (mw loggingMiddleware) Login(ctx context.Context, username, password string) (user users.User, err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "Login",
			"took", time.Since(begin).Microseconds(),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"cust_name", username,
		)
	}(time.Now())
	return mw.next.Login(nil, username, password)
}

func (mw loggingMiddleware) Register(ctx context.Context, username, password, email, first, last string) (string, error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "Register",
			"message", fmt.Sprintf("RegisterInfo: username: %s, email: %s", username, email),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.Register(nil, username, password, email, first, last)
}

func (mw loggingMiddleware) PostUser(ctx context.Context, user users.User) (id string, err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "PostUser",
			"message", fmt.Sprintf("PostUser: username: %s, email: %s userid: %s", user.Username, user.Email, id),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.PostUser(nil, user)
}

func (mw loggingMiddleware) GetUsers(ctx context.Context, id string) (u []users.User, err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		who := id
		if who == "" {
			who = "all"
		}
		mw.logger.Log(
			"method", "GetUsers",
			"message", fmt.Sprintf("GetUsers: userid: %s, size of user: %d", who, len(u)),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.GetUsers(nil, id)
}

func (mw loggingMiddleware) PostAddress(ctx context.Context, add users.Address, id string) (string, error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "PostAddress",
			"message", fmt.Sprintf("PostAddress: userid: %s, street: %s, number: %s", id, add.Street, add.Number),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.PostAddress(nil, add, id)
}

func (mw loggingMiddleware) GetAddresses(ctx context.Context, id string) (a []users.Address, err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		who := id
		if who == "" {
			who = "all"
		}
		mw.logger.Log(
			"method", "GetAddresses",
			"id", who,
			"message", fmt.Sprintf("GetUsers: userid: %s, size of user: %d", who, len(a)),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.GetAddresses(nil, id)
}

func (mw loggingMiddleware) PostCard(ctx context.Context, card users.Card, id string) (string, error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		cc := card
		cc.MaskCC()
		mw.logger.Log(
			"method", "PostCard",
			"message", fmt.Sprintf("PostCard: userid: %s, card: %d", id, cc.LongNum),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.PostCard(nil, card, id)
}

func (mw loggingMiddleware) GetCards(ctx context.Context, id string) (a []users.Card, err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		who := id
		if who == "" {
			who = "all"
		}
		mw.logger.Log(
			"method", "GetCards",
			"message", fmt.Sprintf("GetCards: userid: %s, size of user: %d", who, len(a)),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.GetCards(nil, id)
}

func (mw loggingMiddleware) Delete(ctx context.Context, entity, id string) (err error) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "Delete",
			"entity", entity,
			"id", id,
			"message", fmt.Sprintf("Delete: userid: %s", id),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.Delete(nil, entity, id)
}

func (mw loggingMiddleware) Health(ctx context.Context, logger log.Logger) (health []Health) {
	spanContext := trace.SpanContextFromContext(ctx)
	defer func(begin time.Time) {
		mw.logger.Log(
			"method", "Health",
			"message", len(health),
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"took", time.Since(begin).Milliseconds(),
		)
	}(time.Now())
	return mw.next.Health(nil, logger)
}
