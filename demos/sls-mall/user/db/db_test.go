package db

import (
	"context"
	"errors"
	"reflect"
	"testing"

	"github.com/sls-mis/demos/sls-mall/user/users"
)

var (
	TestDB       = fake{}
	ErrFakeError = errors.New("Fake error")
	TestAddress  = users.Address{
		Street:  "street",
		Number:  "51b",
		Country: "Netherlands",
		City:    "Amsterdam",
		ID:      "000056",
	}
)

func TestInit(t *testing.T) {
	err := Init(nil)
	if err == nil {
		t.Error("Expected no registered db error")
	}
	Register("test", TestDB)
	database = "test"
	err = Init(nil)
	if err != ErrFakeError {
		t.Error("expected fake db error from init")
	}
	TestAddress.AddLinks()
}

func TestSet(t *testing.T) {
	database = "nodb"
	err := Set(nil)
	if err == nil {
		t.Error("Expecting error for no databade found")
	}
	Register("nodb2", TestDB)
	database = "nodb2"
	err = Set(nil)
	if err != nil {
		t.Error(err)
	}
}

func TestRegister(t *testing.T) {
	l := len(DBTypes)
	Register("test2", TestDB)
	if len(DBTypes) != l+1 {
		t.Errorf("Expecting %v DB types received %v", l+1, len(DBTypes))
	}
	l = len(DBTypes)
	Register("test2", TestDB)
	if len(DBTypes) != l {
		t.Errorf("Expecting %v DB types received %v duplicate names", l, len(DBTypes))
	}
}

func TestCreateUser(t *testing.T) {
	err := CreateUser(nil, &users.User{})
	if err != ErrFakeError {
		t.Error("expected fake db error from create")
	}
}

func TestGetUser(t *testing.T) {
	_, err := GetUser(nil, "test")
	if err != ErrFakeError {
		t.Error("expected fake db error from get")
	}
}

func TestGetUserByName(t *testing.T) {
	_, err := GetUserByName(nil, "test")
	if err != ErrFakeError {
		t.Error("expected fake db error from get")
	}
}

func TestGetUserAttributes(t *testing.T) {
	u := users.New()
	GetUserAttributes(nil, &u)
	if len(u.Addresses) != 1 {
		t.Error("expected one address added for GetUserAttributes")
	}
	if !reflect.DeepEqual(u.Addresses[0], TestAddress) {
		t.Error("expected matching addresses")
	}
}

func TestPing(t *testing.T) {
	err := Ping()
	if err != ErrFakeError {
		t.Error("expected fake db error from ping")
	}

}

type fake struct{}

func (f fake) Init() error {
	return ErrFakeError
}
func (f fake) GetUserByName(ctx context.Context, name string) (users.User, error) {
	return users.User{}, ErrFakeError
}
func (f fake) GetUser(ctx context.Context, id string) (users.User, error) {
	return users.User{}, ErrFakeError
}

func (f fake) GetUsers(context.Context) ([]users.User, error) {
	return make([]users.User, 0), ErrFakeError
}

func (f fake) CreateUser(context.Context, *users.User) error {
	return ErrFakeError
}

func (f fake) GetUserAttributes(ctx context.Context, u *users.User) error {
	u.Addresses = append(u.Addresses, TestAddress)
	return nil
}

func (f fake) GetCard(ctx context.Context, id string) (users.Card, error) {
	return users.Card{}, ErrFakeError
}

func (f fake) GetCards(context.Context) ([]users.Card, error) {
	return make([]users.Card, 0), ErrFakeError
}

func (f fake) CreateCard(ctx context.Context, c *users.Card, id string) error {
	return ErrFakeError
}

func (f fake) GetAddress(ctx context.Context, id string) (users.Address, error) {
	return users.Address{}, ErrFakeError
}

func (f fake) GetAddresses(context.Context) ([]users.Address, error) {
	return make([]users.Address, 0), ErrFakeError
}

func (f fake) CreateAddress(ctx context.Context, u *users.Address, id string) error {
	return ErrFakeError
}

func (f fake) Delete(ctx context.Context, entity string, id string) error {
	return ErrFakeError
}

func (f fake) Ping() error {
	return ErrFakeError
}
