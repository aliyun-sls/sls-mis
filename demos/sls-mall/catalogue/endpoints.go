package catalogue

// endpoints.go contains the endpoint definitions, including per-method request
// and response structs. Endpoints are the binding between the service and
// transport.

import (
	"github.com/go-kit/kit/endpoint"
	"golang.org/x/net/context"
)

// Endpoints collects the endpoints that comprise the Service.
type Endpoints struct {
	ListEndpoint   endpoint.Endpoint
	CountEndpoint  endpoint.Endpoint
	GetEndpoint    endpoint.Endpoint
	TagsEndpoint   endpoint.Endpoint
	HealthEndpoint endpoint.Endpoint
}

// MakeEndpoints returns an Endpoints structure, where each endpoint is
// backed by the given service.
func MakeEndpoints(s Service) Endpoints {
	return Endpoints{
		ListEndpoint:   (MakeListEndpoint(s)),
		CountEndpoint:  (MakeCountEndpoint(s)),
		GetEndpoint:    (MakeGetEndpoint(s)),
		TagsEndpoint:   (MakeTagsEndpoint(s)),
		HealthEndpoint: (MakeHealthEndpoint(s)),
	}
}

// MakeListEndpoint returns an endpoint via the given service.
func MakeListEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		req := request.(listRequest)
		socks, err := s.List(ctx, req.Tags, req.Order, req.PageNum, req.PageSize)
		return listResponse{Socks: socks, Err: err}, err
	}
}

// MakeCountEndpoint returns an endpoint via the given service.
func MakeCountEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		req := request.(countRequest)
		n, err := s.Count(ctx, req.Tags)
		return countResponse{N: n, Err: err}, err
	}
}

// MakeGetEndpoint returns an endpoint via the given service.
func MakeGetEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		req := request.(getRequest)
		sock, err := s.Get(ctx, req.ID)
		return getResponse{Sock: sock, Err: err}, err
	}
}

// MakeTagsEndpoint returns an endpoint via the given service.
func MakeTagsEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		tags, err := s.Tags(ctx)
		return tagsResponse{Tags: tags, Err: err}, err
	}
}

// MakeHealthEndpoint returns current health of the given service.
func MakeHealthEndpoint(s Service) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		health := s.Health(ctx)
		return healthResponse{Health: health}, nil
	}
}

type listRequest struct {
	Tags     []string `json:"tags"`
	Order    string   `json:"order"`
	PageNum  int      `json:"pageNum"`
	PageSize int      `json:"pageSize"`
}

type listResponse struct {
	Socks []Sock `json:"sock"`
	Err   error  `json:"err"`
}

type countRequest struct {
	Tags []string `json:"tags"`
}

type countResponse struct {
	N   int   `json:"size"` // to match original
	Err error `json:"err"`
}

type getRequest struct {
	ID string `json:"id"`
}

type getResponse struct {
	Sock Sock  `json:"sock"`
	Err  error `json:"err"`
}

type tagsRequest struct {
	//
}

type tagsResponse struct {
	Tags []string `json:"tags"`
	Err  error    `json:"err"`
}

type healthRequest struct {
	//
}

type healthResponse struct {
	Health []Health `json:"health"`
}
