package infra

import (
	"context"
	"testing"
	"time"

	"github.com/jackc/pgx/v5"
)

var dbTest *pgx.Conn

func TestVotingSaver(t *testing.T) {
	t.Parallel()

	saver := SaveVotingTheme(dbTest)
	id, err := saver.SaveVotingTheme(context.TODO(), "9898989", time.Now(), time.Now().Add(5*time.Hour), `{"a": "b"}`)
	if err != nil {
		t.Errorf("query error: %v", err)
	}

	if id == "" {
		t.Error("id is empty")
	}
}
