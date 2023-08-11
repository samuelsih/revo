package infra

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/jackc/pgx/v5"
)

var dbTest *pgx.Conn

func TestVotingSaver(t *testing.T) {
	saver := SaveVotingTheme(dbTest)
	id, err := saver.SaveVotingTheme(context.TODO(), "9898989", time.Now(), time.Now().Add(5*time.Hour), `{"a": "b"}`)
	if err != nil {
		t.Errorf("query error: %v", err)
	}

	if id == "" {
		t.Error("id is empty")
	}
}

func TestFindVotingTheme(t *testing.T) {
	finder := FindVotingTheme(dbTest)

	t.Run("valid", func(t *testing.T) {
		endAt, err := finder.FindVotingTheme(context.TODO(), "some-id")
		if err != nil {
			t.Errorf("err find: %v", err)
		}

		expect := "2023-08-09 18:45:55.330487 +0000 UTC"
		got := endAt.UTC().String()

		if got != expect {
			t.Errorf("expected %v, got %v", expect, got)
		}
	})

	t.Run("not found", func(t *testing.T) {
		_, err := finder.FindVotingTheme(context.TODO(), "unknown-id")
		if err == nil {
			t.Error("err is nil")
		}

		if !errors.Is(err, ErrVotingThemeNotFound) {
			t.Errorf("expected err %v, got %v", ErrVotingThemeNotFound, err)
		}
	})
}
