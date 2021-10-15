package payment

import (
	"context"
	"errors"
	"fmt"
	"math/rand"
	"time"

	"github.com/go-kit/kit/log"
	"github.com/go-kit/kit/log/level"
	"go.opentelemetry.io/otel/trace"
)

// Middleware decorates a service.
type Middleware func(Service) Service

type Service interface {
	Authorise(ctx context.Context, total float32) (Authorisation, error) // GET /paymentAuth
	Health(ctx context.Context) []Health                                 // GET /health
}

type Authorisation struct {
	Authorised bool   `json:"authorised"`
	Message    string `json:"message"`
}

type Health struct {
	Service string `json:"service"`
	Status  string `json:"status"`
	Time    string `json:"time"`
}

// NewFixedService returns a simple implementation of the Service interface,
// fixed over a predefined set of socks and tags. In a real service you'd
// probably construct this with a database handle to your socks DB, etc.
func NewAuthorisationService(declineOverAmount float32, logger log.Logger) Service {
	return &service{
		declineOverAmount: declineOverAmount,
		logger:            logger,
	}
}

type service struct {
	declineOverAmount float32
	logger            log.Logger
}

func (s *service) deductFromCart(ctx context.Context, amount float32) error {
	spanContext := trace.SpanContextFromContext(ctx)
	level.Info(s.logger).Log("traceId", spanContext.TraceID.String(),
		"spanId", spanContext.SpanID.String(),
		"content", "start deduct from cart",
		"amount", amount)
	if rand.Int()%5 == 0 {
		// mock deduct timeout!!!
		time.Sleep(6 * time.Second)
		err := errors.New("Deduct money from your bank card timeout")
		level.Error(s.logger).Log("traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String(),
			"content", "deduct from cart error",
			"error", err.Error())
		return err
	}
	level.Info(s.logger).Log("traceId", spanContext.TraceID.String(),
		"spanId", spanContext.SpanID.String(),
		"content", "deduct from cart success")
	return nil
}

func (s *service) Authorise(ctx context.Context, amount float32) (Authorisation, error) {
	if amount == 0 {
		return Authorisation{}, ErrInvalidPaymentAmount
	}
	if amount < 0 {
		return Authorisation{}, ErrInvalidPaymentAmount
	}
	authorised := false
	message := "Payment declined"
	if amount <= s.declineOverAmount {
		authorised = true
		message = "Payment authorised"
	} else {
		message = fmt.Sprintf("Payment declined: amount exceeds %.2f", s.declineOverAmount)
	}

	err := s.deductFromCart(ctx, amount)
	if err != nil {

		authorised = false
	}

	return Authorisation{
		Authorised: authorised,
		Message:    message,
	}, err
}

func (s *service) Health(ctx context.Context) []Health {
	var health []Health
	app := Health{"payment", "OK", time.Now().String()}
	health = append(health, app)
	return health
}

var ErrInvalidPaymentAmount = errors.New("Invalid payment amount")
