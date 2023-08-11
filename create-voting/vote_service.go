package main

import (
	"context"
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"io"
	"log/slog"
	"time"
	"unsafe"
)

type Dependencies struct {
	Publisher        Publisher
	Uploader         Uploader
	VotingThemeSaver VotingThemeSaver
}

type Publisher interface {
	Publish(ctx context.Context, msg []byte)
}

type Uploader interface {
	Upload(ctx context.Context, objectName string, file io.Reader) (string, error)
}

type VotingThemeSaver interface {
	SaveVotingTheme(
		ctx context.Context,
		userID string,
		startAt time.Time,
		endAt time.Time,
		metadata string,
	) (string, error)
}

type Output struct {
	Code int    `json:"code"`
	Msg  string `json:"msg"`
	Data any    `json:"data"`
}

func CreateVotingThemeService(ctx context.Context, deps Dependencies, inputs []votingThemeInput) Output {
	user, _ := ctx.Value(ctxKey{}).(User)

	type jsonbMetadata struct {
		Name        string `json:"name"`
		Description string `json:"description"`
		ImgLink     string `json:"img_link"`
	}

	output := Output{
		Code: 201,
		Msg:  "Created",
	}

	for i, input := range inputs {
		errs, ok := validate(i, input)
		if !ok {
			return Output{
				Code: 422,
				Msg:  "Unprocessable entity",
				Data: errs,
			}
		}
	}

	rawMetadata := make([]jsonbMetadata, len(inputs))

	for i, input := range inputs {
		if input.Image == nil {
			rawMetadata[i] = jsonbMetadata{
				Name:        input.Name,
				Description: input.Description,
				ImgLink:     "",
			}
			
			continue
		}

		str := randomStr(16)
		url, err := deps.Uploader.Upload(ctx, str, input.Image)
		if err != nil {
			slog.ErrorContext(ctx, "upload %s - %s: %v", input.Name, input.Description, err)
			return Output{Code: 500, Msg: "Internal Server Error"}
		}

		rawMetadata[i] = jsonbMetadata{
			Name:        input.Name,
			Description: input.Description,
			ImgLink:     url,
		}
	}

	jsonb, _ := json.Marshal(rawMetadata)

	result, err := deps.VotingThemeSaver.SaveVotingTheme(
		ctx,
		user.ID,
		time.Now().UTC(),
		time.Now().UTC().Add(5*time.Hour),
		b2s(jsonb),
	)
	if err != nil {
		slog.ErrorContext(ctx, "db error: %v", err)
		return Output{Code: 500, Msg: "Internal Server Error"}
	}

	output.Data = map[string]any{
		"metadata": rawMetadata,
		"id":       result,
	}

	return output
}

func validate(index int, input votingThemeInput) (map[string]any, bool) {
	m := make(map[string]any, 3)
	ok := true

	m["idx"] = index

	if len(input.Name) < 3 || len(input.Name) > 80 {
		m["name"] = "Name length must between 3 and 80 char(s)"
		ok = false
	}

	if len(input.Description) < 3 || len(input.Description) > 255 {
		m["description"] = "Description length must between 3 and 255 char(s)"
		ok = false
	}

	return m, ok
}

func randomStr(length int) string {
	randomBytes := make([]byte, length)
	rand.Read(randomBytes) //nolint:errcheck // length is always > 0
	return base64.URLEncoding.EncodeToString(randomBytes)
}

func b2s(b []byte) string {
	return unsafe.String(unsafe.SliceData(b), len(b))
}
