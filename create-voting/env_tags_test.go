package main

import (
	"errors"
	"testing"
)

func TestFill(t *testing.T) {
	t.Run("happy path", func(t *testing.T) {
		type S struct {
			Name   string `env:"name" default:"anjay"`
			Age    string `env:"age" default:"23"`
			Addr   string `env:"addr" default:"mamang 123"`
			IsTrue bool   `env:"istrue" default:"true"`
		}

		var s S

		err := FillEnv(&s)
		if err != nil {
			t.Error(err)
		}

		expected := S{
			Name:   "anjay",
			Age:    "23",
			Addr:   "mamang 123",
			IsTrue: true,
		}

		if s != expected {
			t.Logf("expected %v, got %v", expected, s)
			t.Fail()
		}
	})

	t.Run("not a ptr type", func(t *testing.T) {
		type S struct {
			Name string `env:"name" default:"asyu"`
		}

		var s S

		err := FillEnv(s)

		if !errors.Is(err, errInvalidPtrType) {
			t.Errorf("wrong err type, must be ptr: %v", err)
			t.Fail()
		}
	})

	t.Run("not a struct type", func(t *testing.T) {
		var s string

		err := FillEnv(&s)

		if !errors.Is(err, errInvalidStructType) {
			t.Errorf("wrong err type, must be struct: %v", err)
			t.Fail()
		}
	})
}
