package chaos

import (
	"context"
	"errors"
	"flag"
	"github.com/go-kit/kit/log"
	"github.com/sls-mis/demos/sls-mall/user/api"
	"github.com/sls-mis/demos/sls-mall/user/users"
	"math/rand"
	"os"
	"strconv"
	"time"
)

var (
	client_max_sleep_time_us string
	client_min_sleep_time_us string
	slow_p                   string
	throw_exception_p        string
	server_min_sleep_time_us string
	server_max_sleep_time_us string
)

type ChaosConfiguration struct {
	client_max_sleep_time_us int64
	client_min_sleep_time_us int64
	server_min_sleep_time_us int64
	slow_p                   int
	throw_exception_p        int
	server_max_sleep_time_us int64
}

func init() {
	flag.StringVar(&client_max_sleep_time_us, "client_max_sleep_time_us", os.Getenv("CLIENT_MAX_SLEEP_TIME_US"), "")
	flag.StringVar(&client_min_sleep_time_us, "client_min_sleep_time_us", os.Getenv("CLIENT_MIN_SLEEP_TIME_US"), "")
	flag.StringVar(&slow_p, "slow_p", os.Getenv("SLOW_P"), "")
	flag.StringVar(&server_min_sleep_time_us, "server_min_sleep_time_us", os.Getenv("SERVER_MIN_SLEEP_TIME_US"), "")
	flag.StringVar(&throw_exception_p, "throw_exception_p", os.Getenv("THROW_EXCEPTION_P"), "")
	flag.StringVar(&server_max_sleep_time_us, "server_max_sleep_time_us", os.Getenv("SERVER_MAX_SLEEP_TIME_US"), "")
}

type ChaosMiddleware struct {
	config *ChaosConfiguration
	next   api.Service
}

func NewChaosMiddleware(configuration *ChaosConfiguration) api.Middleware {
	return func(next api.Service) api.Service {
		return ChaosMiddleware{
			config: configuration,
			next:   next,
		}
	}
}

func (c ChaosMiddleware) Login(ctx context.Context, username, password string) (users.User, error) {
	return c.next.Login(ctx, username, password)
}

func (c ChaosMiddleware) Register(ctx context.Context, username, password, email, first, last string) (string, error) {
	return c.next.Register(ctx, username, password, email, first, last)
}

func (c ChaosMiddleware) GetUsers(ctx context.Context, id string) ([]users.User, error) {
	if rand.Intn(100) > (1 - c.config.slow_p) {
		time.Sleep(time.Duration(rand.Int63n(c.config.server_max_sleep_time_us-c.config.server_min_sleep_time_us)+c.config.server_min_sleep_time_us) * time.Microsecond)
	}

	if rand.Intn(100) > (1 - c.config.throw_exception_p) {
		return nil, errors.New("Mock Exception")
	}
	return c.next.GetUsers(ctx, id)
}

func (c ChaosMiddleware) PostUser(ctx context.Context, u users.User) (string, error) {
	return c.next.PostUser(ctx, u)
}

func (c ChaosMiddleware) GetAddresses(ctx context.Context, id string) ([]users.Address, error) {
	return c.next.GetAddresses(ctx, id)
}

func (c ChaosMiddleware) PostAddress(ctx context.Context, u users.Address, userid string) (string, error) {
	return c.next.PostAddress(ctx, u, userid)
}

func (c ChaosMiddleware) GetCards(ctx context.Context, id string) ([]users.Card, error) {
	if rand.Intn(100) > c.config.slow_p {
		time.Sleep(time.Duration(rand.Int63n(c.config.server_max_sleep_time_us-c.config.server_min_sleep_time_us)+c.config.server_min_sleep_time_us) * time.Microsecond)
	}

	if rand.Intn(100) > c.config.throw_exception_p {
		return nil, errors.New("Mock Exception")
	}
	return c.next.GetCards(ctx, id)
}

func (c ChaosMiddleware) PostCard(ctx context.Context, u users.Card, userid string) (string, error) {
	return c.next.PostCard(ctx, u, userid)
}

func (c ChaosMiddleware) Delete(ctx context.Context, entity, id string) error {
	return c.next.Delete(ctx, entity, id)
}

func (c ChaosMiddleware) Health(ctx context.Context, logger log.Logger) []api.Health {
	return c.next.Health(ctx, logger)
}

func NewConfiguration() *ChaosConfiguration {

	return &ChaosConfiguration{
		client_max_sleep_time_us: func() int64 {
			if r, e := strconv.ParseInt(client_max_sleep_time_us, 10, 64); e != nil {
				return 0
			} else {
				return r
			}
		}(),
		client_min_sleep_time_us: func() int64 {
			if r, e := strconv.ParseInt(client_min_sleep_time_us, 10, 64); e != nil {
				return 0
			} else {
				return r
			}
		}(),
		slow_p: func() int {
			if r, e := strconv.ParseInt(slow_p, 10, 64); e != nil {
				return 0
			} else {
				return int(r)
			}
		}(),
		server_min_sleep_time_us: func() int64 {
			if r, e := strconv.ParseInt(server_min_sleep_time_us, 10, 64); e != nil {
				return 0
			} else {
				return r
			}
		}(),
		throw_exception_p: func() int {
			if r, e := strconv.ParseInt(throw_exception_p, 10, 32); e != nil {
				return 0
			} else {
				return int(r)
			}
		}(),
		server_max_sleep_time_us: func() int64 {
			if r, e := strconv.ParseInt(server_max_sleep_time_us, 10, 64); e != nil {
				return 0
			} else {
				return r
			}
		}(),
	}
}
