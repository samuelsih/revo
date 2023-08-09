package main

import (
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	_ "embed"
)

func Test_serializeSuccess(t *testing.T) {
	type someData struct {
		Name string `json:"name"`
		Age  int    `json:"age"`
	}

	data := someData{Name: "bob", Age: 14}

	b, err := json.Marshal(data)
	if err != nil {
		t.Fatalf("Unable to marshal: %v", err)
	}

	body := strings.NewReader(string(b))

	r := httptest.NewRequest(http.MethodGet, "/", body)

	var dst someData

	err = serialize(r.Body, &dst)
	if err != nil {
		t.Fatalf("Unable to serialize: %v", err)
	}
}

func Test_EmptyBody(t *testing.T) {
	body := strings.NewReader("")
	var data map[string]any

	r := httptest.NewRequest(http.MethodGet, "/", body)

	err := serialize(r.Body, &data)
	if err.Error() != "body must not be empty" {
		t.Fatalf("expected %v, got %v", "body must not be empty", err)
	}
}

func Test_SyntaxError(t *testing.T) {
	body := strings.NewReader(
		`{
			123: 123
		 }`,
	)

	var data map[string]any

	r := httptest.NewRequest(http.MethodGet, "/", body)

	var syntaxError *json.SyntaxError

	err := serialize(r.Body, &data)
	if errors.As(err, &syntaxError) {
		t.Fatalf("expected %v, got %v", syntaxError.Error(), err)
	}
}

func Test_UnmarshalTypeError(t *testing.T) {
	body := strings.NewReader(
		`{
			"name": false
		 }`,
	)

	type A struct {
		Name string `json:"name"`
	}

	var data A

	r := httptest.NewRequest(http.MethodGet, "/", body)

	err := serialize(r.Body, &data)

	if !strings.HasPrefix(err.Error(), "body contains incorrect JSON") {
		t.Fatalf("expected %v, got %v", "body contains incorrect JSON", err)
	}
}

func Test_UnknownFields(t *testing.T) {
	body := strings.NewReader(`
		{
			"names": "Bob"
		}
	`)

	type A struct {
		Name string `json:"name"`
	}

	var data A

	r := httptest.NewRequest(http.MethodGet, "/", body)

	err := serialize(r.Body, &data)
	if !strings.HasPrefix(err.Error(), "body contains unknown key ") {
		t.Fatalf("expected %v, got %v", "body contains unknown key", err)
	}
}

func Test_InvalidUnmarshalError(t *testing.T) {
	body := strings.NewReader(`
	{
		"name": "foo"
	}`)

	var data map[string]any

	r := httptest.NewRequest(http.MethodGet, "/foo", body)

	err := serialize(r.Body, data)

	if err.Error() != "can't convert json request" {
		t.Fatalf("expected %v, got %v", "can't convert json request", err)
	}
}

func Test_SingleJSONValue(t *testing.T) {
	in := `
		{
			"name": "Foo"
		}
		{
			"age": "bar"
		}
	`

	body := strings.NewReader(in)

	r := httptest.NewRequest(http.MethodGet, "/foo", body)

	var data map[string]any

	err := serialize(r.Body, &data)

	if err.Error() != "body must only contain a single JSON value" {
		t.Fatalf("expected %v, got %v", "body must only contain a single JSON value", err)
	}
}

func Test_BadFormedJSON(t *testing.T) {
	badlyFormedJSON := `{"foo": "bar"`
	err := serialize(strings.NewReader(badlyFormedJSON), struct{}{})
	if err != nil && err.Error() != "body contains badly-formed JSON" {
		t.Errorf("Expected error for body that contains badly-formed JSON")
	}
}
