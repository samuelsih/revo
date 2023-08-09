package infra

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/rs/xid"
)

type SaveVotingThemeFunc func(
	ctx context.Context,
	userID string,
	startAt time.Time,
	endAt time.Time,
	metadata string,
) (string, error)

func (s SaveVotingThemeFunc) SaveVotingTheme(
	ctx context.Context,
	userID string,
	startAt time.Time,
	endAt time.Time,
	metadata string,
) (string, error) {
	return s(ctx, userID, startAt, endAt, metadata)
}

const query = `
INSERT INTO voting_theme (id, user_id, start_at, end_at, metadata)
VALUES ($1, $2, $3, $4, $5)
RETURNING id
;
`

func SaveVotingTheme(db *pgx.Conn) SaveVotingThemeFunc {
	return func(ctx context.Context, userID string, startAt, endAt time.Time, metadata string) (string, error) {
		id := xid.New().String()

		row := db.QueryRow(ctx, query, id, userID, startAt, endAt, metadata)

		var result string

		err := row.Scan(&result)

		return result, err
	}
}
