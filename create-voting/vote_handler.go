package main

import (
	"bytes"
	"errors"
	"io"
	"log/slog"
	"mime/multipart"
	"net/http"
	"strings"

	"github.com/samuelsih/httpwr"
)

const (
	oneMB       = 1024 * 1024
	minMetadata = 2
	maxMetadata = 4
)

var (
	jpegBytes     = []byte{0xFF, 0xD8}
	jpgBytes      = []byte{0xFF, 0xE0}
	pngBytes      = []byte{0x89, 0x50}
	emptyMetadata = metadata{}
)

var (
	errInvalidImageType = errors.New("invalid image type")
	errSizeImage        = errors.New("size cannot more than 1MB")
)

type M map[string]any

type metadata struct {
	Name        string `json:"name"`
	Description string `json:"description,omitempty"`
	ImgKey      string `json:"img,omitempty"`
}

type allMetadata struct {
	Metadata [maxMetadata]metadata `json:"metadata"`
}

type votingThemeInput struct {
	Name        string
	Description string
	Image       multipart.File
}

func CreateVotingThemeHandler(deps Dependencies) http.HandlerFunc {
	var formInput allMetadata

	return httpwr.HandlerFn(func(w http.ResponseWriter, r *http.Request) error {
		r.Body = http.MaxBytesReader(w, r.Body, 5*oneMB)

		err := r.ParseMultipartForm(5 * oneMB)
		if err != nil {
			slog.Error("Parse Error: %v", err)
			return httpwr.Wrap(http.StatusRequestEntityTooLarge, err)
		}

		reader := strings.NewReader(r.FormValue("metadata"))
		err = serialize(reader, &formInput)
		if err != nil {
			slog.Error("Decode error: %v", err)
			return httpwr.Wrap(http.StatusBadRequest, err)
		}

		if !validMetadata(formInput) {
			slog.Error("Invalid metadata")
			return httpwr.Wrap(http.StatusBadRequest, errors.New("invalid metadata"))
		}

		input, err := metadataToInputs(r, formInput)
		if err != nil {
			return httpwr.Wrap(http.StatusBadRequest, err)
		}

		result := CreateVotingThemeService(r.Context(), deps, input)

		return httpwr.OKWithData(w, result.Code, result.Msg, result.Data)
	})
}

func validMetadata(input allMetadata) bool {
	for i := 0; i < maxMetadata; i++ {
		in := input.Metadata[i]

		if in == emptyMetadata {
			if i < minMetadata-1 {
				return false
			}

			break
		}
	}

	return true
}

func metadataToInputs(r *http.Request, in allMetadata) ([]votingThemeInput, error) {
	var out []votingThemeInput

	for _, data := range in.Metadata {
		var i votingThemeInput

		if data == emptyMetadata {
			return out, nil
		}

		i.Name = data.Name
		i.Description = data.Description

		file, handler, err := r.FormFile(data.ImgKey)
		if err != nil {
			i.Image = nil
			out = append(out, i)
			continue
		}

		err = validImage(file, handler.Size)
		if err != nil {
			return out, err
		}

		i.Image = file

		out = append(out, i)
	}

	return out, nil
}

func validImage(imgFile io.ReadSeeker, imgSize int64) error {
	if imgSize > oneMB {
		return errSizeImage
	}

	b, err := getFirstTwoBytes(imgFile)
	if err != nil {
		return errInvalidImageType
	}

	if !isTypeAllowed(b) {
		return errInvalidImageType
	}

	return nil
}

func getFirstTwoBytes(file io.ReadSeeker) ([]byte, error) {
	b := make([]byte, 2)

	_, err := file.Read(b)
	if err != nil {
		return nil, errors.New("failed to check file")
	}

	_, err = file.Seek(0, 0)
	if err != nil {
		return nil, errors.New("failed to read file")
	}

	return b, err
}

func isTypeAllowed(b []byte) bool {
	if b == nil {
		return false
	}

	if len(b) == 0 {
		return false
	}

	if bytes.Equal(b, jpegBytes) {
		return true
	}

	if bytes.Equal(b, pngBytes) {
		return true
	}

	if bytes.Equal(b, jpgBytes) {
		return true
	}

	return false
}
