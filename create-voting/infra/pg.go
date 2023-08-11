package infra

import (
	"context"
	"errors"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/rs/xid"
)

var (
	ErrVotingThemeNotFound = errors.New("unknown voting")
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

const saveVotingQuery = `
INSERT INTO voting_theme (id, user_id, start_at, end_at, metadata)
VALUES ($1, $2, $3, $4, $5)
RETURNING id
;
`

func SaveVotingTheme(db *pgx.Conn) SaveVotingThemeFunc {
	return func(ctx context.Context, userID string, startAt, endAt time.Time, metadata string) (string, error) {
		id := xid.New().String()

		row := db.QueryRow(ctx, saveVotingQuery, id, userID, startAt, endAt, metadata)

		var result string

		err := row.Scan(&result)

		return result, err
	}
}

type FindVotingThemeFunc func(ctx context.Context, id string) (time.Time, error)

func (f FindVotingThemeFunc) FindVotingTheme(ctx context.Context, id string) (time.Time, error) {
	return f(ctx, id)
}

const findThemeQuery = `
SELECT end_at
FROM voting_theme
WHERE id = $1
;
`

func FindVotingTheme(db *pgx.Conn) FindVotingThemeFunc {
	return func(ctx context.Context, id string) (time.Time, error) {
		var result time.Time

		err := db.QueryRow(ctx, findThemeQuery, id).Scan(&result)

		if err != nil {
			if errors.Is(err, pgx.ErrNoRows) {
				return result, ErrVotingThemeNotFound
			}

			return result, err
		}

		return result, nil
	}
}
