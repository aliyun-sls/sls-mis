package api

// endpoints.go contains the endpoint definitions, including per-method request
// and response structs. Endpoints are the binding between the service and
// transport.

import (
	"context"
	"github.com/go-kit/kit/endpoint"
	"github.com/go-kit/kit/log"
	"github.com/sls-mis/demos/sls-mall/user/db"
	"github.com/sls-mis/demos/sls-mall/user/opentelemetry"
	"github.com/sls-mis/demos/sls-mall/user/users"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/label"
)

// Endpoints collects the endpoints that comprise the Service.
type Endpoints struct {
	LoginEndpoint       endpoint.Endpoint
	RegisterEndpoint    endpoint.Endpoint
	UserGetEndpoint     endpoint.Endpoint
	UserPostEndpoint    endpoint.Endpoint
	AddressGetEndpoint  endpoint.Endpoint
	AddressPostEndpoint endpoint.Endpoint
	CardGetEndpoint     endpoint.Endpoint
	CardPostEndpoint    endpoint.Endpoint
	DeleteEndpoint      endpoint.Endpoint
	HealthEndpoint      endpoint.Endpoint
}

// MakeEndpoints returns an Endpoints structure, where each endpoint is
// backed by the given service.
func MakeEndpoints(s Service, logger log.Logger) Endpoints {
	return Endpoints{
		LoginEndpoint:       opentelemetry.TraceServer("GET /login")(MakeLoginEndpoint(s)),
		RegisterEndpoint:    opentelemetry.TraceServer("POST /register")(MakeRegisterEndpoint(s)),
		HealthEndpoint:      opentelemetry.TraceServer("GET /health")(MakeHealthEndpoint(s, logger)),
		UserGetEndpoint:     opentelemetry.TraceServer("GET /customers")(MakeUserGetEndpoint(s)),
		UserPostEndpoint:    opentelemetry.TraceServer("POST /customers")(MakeUserPostEndpoint(s)),
		AddressGetEndpoint:  opentelemetry.TraceServer("GET /addresses")(MakeAddressGetEndpoint(s)),
		AddressPostEndpoint: opentelemetry.TraceServer("POST /addresses")(MakeAddressPostEndpoint(s)),
		CardGetEndpoint:     opentelemetry.TraceServer("GET /cards")(MakeCardGetEndpoint(s)),
		DeleteEndpoint:      opentelemetry.TraceServer("DELETE /")(MakeDeleteEndpoint(s)),
		CardPostEndpoint:    opentelemetry.TraceServer("POST /cards")(MakeCardPostEndpoint(s)),
	}
}

// MakeLoginEndpoint returns an endpoint via the given service.
func MakeLoginEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		ctx, span := otel.Tracer("").Start(ctx, "login user")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		req := request.(loginRequest)
		u, err := s.Login(ctx, req.Username, req.Password)
		return userResponse{User: u}, err
	}
}

// MakeRegisterEndpoint returns an endpoint via the given service.
func MakeRegisterEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		ctx, span := otel.Tracer("").Start(ctx, "register user")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		req := request.(registerRequest)
		id, err := s.Register(ctx, req.Username, req.Password, req.Email, req.FirstName, req.LastName)
		return postResponse{ID: id}, err
	}
}

// MakeUserGetEndpoint returns an endpoint via the given service.
func MakeUserGetEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		newCtx, span := otel.Tracer("").Start(ctx, "get users")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		req := request.(GetRequest)

		_, mongoSpan := otel.Tracer("").Start(newCtx, "users from db")
		usrs, err := s.GetUsers(ctx, req.ID)
		mongoSpan.End()
		if req.ID == "" {
			return EmbedStruct{usersResponse{Users: usrs}}, err
		}
		if len(usrs) == 0 {
			if req.Attr == "addresses" {
				return EmbedStruct{addressesResponse{Addresses: make([]users.Address, 0)}}, err
			}
			if req.Attr == "cards" {
				return EmbedStruct{cardsResponse{Cards: make([]users.Card, 0)}}, err
			}
			return users.User{}, err
		}
		user := usrs[0]
		_, attributeSpan := otel.Tracer("").Start(newCtx, "attributes from db")
		db.GetUserAttributes(ctx, &user)
		attributeSpan.End()
		if req.Attr == "addresses" {
			return EmbedStruct{addressesResponse{Addresses: user.Addresses}}, err
		}
		if req.Attr == "cards" {
			return EmbedStruct{cardsResponse{Cards: user.Cards}}, err
		}
		return user, err
	}
}

// MakeUserPostEndpoint returns an endpoint via the given service.
func MakeUserPostEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		_, span := otel.Tracer("").Start(ctx, "post users")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		req := request.(users.User)
		id, err := s.PostUser(ctx, req)
		return postResponse{ID: id}, err
	}
}

// MakeAddressGetEndpoint returns an endpoint via the given service.
func MakeAddressGetEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		newCtx, getSpan := otel.Tracer("").Start(ctx, "get users")
		getSpan.SetAttributes(label.String("service", "user"))
		defer getSpan.End()
		req := request.(GetRequest)
		_, attributeSpan := otel.Tracer("").Start(newCtx, "addresses from db")
		adds, err := s.GetAddresses(ctx, req.ID)
		attributeSpan.End()
		if req.ID == "" {
			return EmbedStruct{addressesResponse{Addresses: adds}}, err
		}
		if len(adds) == 0 {
			return users.Address{}, err
		}
		return adds[0], err
	}
}

// MakeAddressPostEndpoint returns an endpoint via the given service.
func MakeAddressPostEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		_, postSpan := otel.Tracer("").Start(ctx, "post address")
		postSpan.SetAttributes(label.String("service", "user"))
		defer postSpan.End()
		req := request.(addressPostRequest)
		id, err := s.PostAddress(ctx, req.Address, req.UserID)
		return postResponse{ID: id}, err
	}
}

// MakeUserGetEndpoint returns an endpoint via the given service.
func MakeCardGetEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		newCtx, getSpan := otel.Tracer("").Start(ctx, "get cards")
		getSpan.SetAttributes(label.String("service", "user"))
		defer getSpan.End()
		req := request.(GetRequest)

		_, span := otel.Tracer("").Start(newCtx, "addresses from db")
		span.SetAttributes(label.String("service", "user"))
		cards, err := s.GetCards(ctx, req.ID)
		span.End()
		if req.ID == "" {
			return EmbedStruct{cardsResponse{Cards: cards}}, err
		}
		if len(cards) == 0 {
			return users.Card{}, err
		}
		return cards[0], err
	}
}

// MakeCardPostEndpoint returns an endpoint via the given service.
func MakeCardPostEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		_, span := otel.Tracer("").Start(ctx, "post card")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		req := request.(cardPostRequest)
		id, err := s.PostCard(ctx, req.Card, req.UserID)
		return postResponse{ID: id}, err
	}
}

// MakeLoginEndpoint returns an endpoint via the given service.
func MakeDeleteEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		_, span := otel.Tracer("").Start(ctx, "delete entity")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		req := request.(deleteRequest)
		err = s.Delete(ctx, req.Entity, req.ID)
		if err == nil {
			return statusResponse{Status: true}, err
		}
		return statusResponse{Status: false}, err
	}
}

// MakeHealthEndpoint returns current health of the given service.
func MakeHealthEndpoint(s Service, logger log.Logger) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		_, span := otel.Tracer("").Start(ctx, "health check")
		span.SetAttributes(label.String("service", "user"))
		defer span.End()
		newLogger := log.With(logger, "traceId", func() log.Valuer {
			return func() interface{} {
				return span.SpanContext().TraceID
			}
		}())

		health := s.Health(ctx, log.With(newLogger, "spanId", func() log.Valuer {
			return func() interface{} {
				return span.SpanContext().SpanID
			}
		}()))
		return healthResponse{Health: health}, nil
	}
}

type GetRequest struct {
	ID   string
	Attr string
}

type loginRequest struct {
	Username string
	Password string
}

type userResponse struct {
	User users.User `json:"user"`
}

type usersResponse struct {
	Users []users.User `json:"customer"`
}

type addressPostRequest struct {
	users.Address
	UserID string `json:"userID"`
}

type addressesResponse struct {
	Addresses []users.Address `json:"address"`
}

type cardPostRequest struct {
	users.Card
	UserID string `json:"userID"`
}

type cardsResponse struct {
	Cards []users.Card `json:"card"`
}

type registerRequest struct {
	Username  string `json:"username"`
	Password  string `json:"password"`
	Email     string `json:"email"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
}

type statusResponse struct {
	Status bool `json:"status"`
}

type postResponse struct {
	ID string `json:"id"`
}

type deleteRequest struct {
	Entity string
	ID     string
}

type healthRequest struct {
	//
}

type healthResponse struct {
	Health []Health `json:"health"`
}

type EmbedStruct struct {
	Embed interface{} `json:"_embedded"`
}
