package payment

import (
	"context"
	"errors"
	"fmt"
	"math/rand"
	"time"

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
func NewAuthorisationService(declineOverAmount float32) Service {
	return &service{
		declineOverAmount: declineOverAmount,
	}
}

type service struct {
	declineOverAmount float32
}

func deductFromCart(amount float32) error {
	if rand.Int()%10 == 0 {
		time.Sleep(time.Second)
		return errors.New("Deduct money from your bank card timeout")
	}
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

	err := deductFromCart(amount)
	if err != nil {
		authorised = false
		spanContext := trace.SpanContextFromContext(ctx)
		globalLogger.Log("error", err.Error(),
			"Authorised", authorised,
			"traceId", spanContext.TraceID.String(),
			"spanId", spanContext.SpanID.String())
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
