package infra

import (
	"context"
	"errors"
	"fmt"
	"log/slog"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/rs/xid"
	"gopkg.in/guregu/null.v4"
)

var ErrVotingThemeNotFound = errors.New("unknown voting")

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

func SaveVotingTheme(db *pgxpool.Pool) SaveVotingThemeFunc {
	return func(ctx context.Context, userID string, startAt, endAt time.Time, metadata string) (string, error) {
		id := xid.New().String()

		row := db.QueryRow(ctx, saveVotingQuery, id, userID, startAt, endAt, metadata)

		var result string

		err := row.Scan(&result)

		return result, err
	}
}

type FindVotingThemeFunc func(ctx context.Context, id string, pos int) (time.Time, FindVotingMetadata, error)

func (f FindVotingThemeFunc) FindVotingTheme(
	ctx context.Context,
	id string,
	pos int,
) (time.Time, FindVotingMetadata, error) {
	return f(ctx, id, pos)
}

type FindVotingMetadata = struct {
	Name        null.String `name:"name"`
	Description null.String `name:"description"`
	ImgLink     null.String `name:"img_link"`
}

const findThemeQuery = `
SELECT end_at, metadata->%d->'name', metadata->%d->'description', metadata->%d->'img_link'
FROM voting_theme
WHERE id = $1
;
`

func FindVotingTheme(db *pgxpool.Pool) FindVotingThemeFunc {
	return func(ctx context.Context, id string, pos int) (time.Time, FindVotingMetadata, error) {
		q := fmt.Sprintf(findThemeQuery, pos, pos, pos)

		var findVotingTheme FindVotingMetadata
		var endAt time.Time

		err := db.QueryRow(ctx, q, id).Scan(
			&endAt,
			&findVotingTheme.Name,
			&findVotingTheme.Description,
			&findVotingTheme.ImgLink,
		)

		if err != nil {
			if errors.Is(err, pgx.ErrNoRows) {
				return endAt, findVotingTheme, ErrVotingThemeNotFound
			}

			slog.Error(err.Error())
			return endAt, findVotingTheme, err
		}

		return endAt, findVotingTheme, nil
	}
}
