package main

import (
	"bytes"
	"errors"
	"fmt"
	"io"
	"mime/multipart"
	"net/http"
	"os"
	"path/filepath"
	"testing"
)

func TestGetFirstTwoBytes(t *testing.T) {
	mockReader := &MockReader{
		Bytes: []byte("AB"),
	}

	bytes, err := getFirstTwoBytes(mockReader)
	if err != nil {
		t.Errorf("Error getting first two bytes: %v", err)
	}

	if bytes[0] != 'A' || bytes[1] != 'B' {
		t.Errorf("Expected bytes 'AB', got %v", bytes)
	}
}

func TestIsTypeAllowed(t *testing.T) {
	t.Run("Valid Types", func(t *testing.T) {
		if !isTypeAllowed(jpegBytes) {
			t.Errorf("Expected true for jpegBytes")
		}

		if !isTypeAllowed(pngBytes) {
			t.Errorf("Expected true for pngBytes")
		}

		if !isTypeAllowed(jpgBytes) {
			t.Errorf("Expected true for jpgBytes")
		}
	})

	t.Run("Invalid Types", func(t *testing.T) {
		if isTypeAllowed(nil) {
			t.Errorf("Expected false for nil slice")
		}

		if isTypeAllowed([]byte{}) {
			t.Errorf("Expected false for empty slice")
		}

		if isTypeAllowed([]byte{0xFF}) {
			t.Errorf("Expected false for single byte")
		}

		if isTypeAllowed([]byte{0xFF, 0xD8, 0x00}) {
			t.Errorf("Expected false for invalid byte sequence")
		}
	})
}

type MockReader struct {
	Bytes      []byte
	dataOffset int64
}

func (m *MockReader) Read(p []byte) (int, error) {
	if len(m.Bytes) == 0 {
		return 0, io.EOF
	}

	n := copy(p, m.Bytes)
	m.Bytes = m.Bytes[n:]

	m.dataOffset += int64(n)

	return n, nil
}

func (m *MockReader) Seek(offset int64, whence int) (int64, error) {
	switch whence {
	case io.SeekStart:
		return offset, nil
	case io.SeekCurrent:
		return offset + m.dataOffset, nil
	case io.SeekEnd:
		return int64(len(m.Bytes)) + offset, nil
	}
	return 0, fmt.Errorf("invalid whence value: %d", whence)
}

func TestValidMetadata(t *testing.T) {
	type args struct {
		input allMetadata
	}
	tests := []struct {
		name string
		args args
		want bool
	}{
		{
			name: "empty metadata",
			args: args{
				input: allMetadata{},
			},
			want: false,
		},
		{
			name: "not full metadata should still true",
			args: args{
				input: allMetadata{
					[4]metadata{
						{
							Name:        "Ngab Owi",
							Description: "Presiden",
							ImgKey:      "owi",
						},
						{
							Name:        "Donald Bebek",
							Description: "Seorang bebek",
							ImgKey:      "donal",
						},
					},
				},
			},
			want: true,
		},
		{
			name: "full metadata should true",
			args: args{
				input: allMetadata{
					[4]metadata{
						{
							Name:        "Ngab Owi",
							Description: "Presiden",
							ImgKey:      "owi",
						},
						{
							Name:        "Donald Bebek",
							Description: "Seorang bebek",
							ImgKey:      "donal",
						},
						{
							Name:        "Bocil",
							Description: "Bocil",
							ImgKey:      "bocil",
						},
						{
							Name:        "Scaloni",
							Description: "Pelatih",
							ImgKey:      "scaloni",
						},
					},
				},
			},
			want: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := validMetadata(tt.args.input); got != tt.want {
				t.Errorf("validMetadata() = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestMetadataToInputsValid(t *testing.T) {
	body := new(bytes.Buffer)
	w := multipart.NewWriter(body)

	WithFile(t, w, "testfiles/small.png", "anjay")

	err := w.Close()
	if err != nil {
		t.Fatalf("w close: %v", err)
	}

	r, err := http.NewRequest(http.MethodPost, "/sss", body)
	r.Header.Add("Content-Type", w.FormDataContentType())

	if err != nil {
		t.Fatalf("req: %v", err)
	}

	m := allMetadata{
		[4]metadata{
			{
				Name:        "suki",
				Description: "sukite yo",
				ImgKey:      "anjay",
			},
		},
	}

	res, err := metadataToInputs(r, m)
	if err != nil {
		t.Fatalf("metadataToInputs: %v", err)
	}

	if res[0].Name != "suki" {
		t.Fatalf("expected suki, got %s", res[0].Name)
	}

	if res[0].Description != "sukite yo" {
		t.Fatalf("expected sukite yo, got %s", res[0].Description)
	}

	if res[0].Image == nil {
		t.Fatal("img is nil")
	}
}

func TestMetadataBigImage(t *testing.T) {
	body := new(bytes.Buffer)
	w := multipart.NewWriter(body)

	WithFile(t, w, "testfiles/big.png", "anjay")

	err := w.Close()
	if err != nil {
		t.Fatalf("w close: %v", err)
	}

	r, err := http.NewRequest(http.MethodPost, "/sss", body)
	r.Header.Add("Content-Type", w.FormDataContentType())

	if err != nil {
		t.Fatalf("req: %v", err)
	}

	m := allMetadata{
		[4]metadata{
			{
				Name:        "suki",
				Description: "sukite yo",
				ImgKey:      "anjay",
			},
		},
	}

	_, err = metadataToInputs(r, m)
	if err == nil {
		t.Fatal("expected error")
	}

	if !errors.Is(err, errSizeImage) {
		t.Fatalf("expected error: %v, got: %v", errSizeImage, err)
	}
}

func TestMetadataNotImage(t *testing.T) {
	body := new(bytes.Buffer)
	w := multipart.NewWriter(body)

	WithFile(t, w, "testfiles/text.txt", "anjay")

	err := w.Close()
	if err != nil {
		t.Fatalf("w close: %v", err)
	}

	r, err := http.NewRequest(http.MethodPost, "/sss", body)
	r.Header.Add("Content-Type", w.FormDataContentType())

	if err != nil {
		t.Fatalf("req: %v", err)
	}

	m := allMetadata{
		[4]metadata{
			{
				Name:        "suki",
				Description: "sukite yo",
				ImgKey:      "anjay",
			},
		},
	}

	_, err = metadataToInputs(r, m)
	if err == nil {
		t.Fatal("expected error")
	}

	if !errors.Is(err, errInvalidImageType) {
		t.Fatalf("expected error: %v, got: %v", errInvalidImageType, err)
	}
}

func TestMetadataMismatchKeyImage(t *testing.T) {
	body := new(bytes.Buffer)
	w := multipart.NewWriter(body)

	WithFile(t, w, "testfiles/text.txt", "anjay")

	err := w.Close()
	if err != nil {
		t.Fatalf("w close: %v", err)
	}

	r, err := http.NewRequest(http.MethodPost, "/sss", body)
	r.Header.Add("Content-Type", w.FormDataContentType())

	if err != nil {
		t.Fatalf("req: %v", err)
	}

	m := allMetadata{
		[4]metadata{
			{
				Name:        "suki",
				Description: "sukite yo",
				ImgKey:      "bukan_anjay",
			},
		},
	}

	res, err := metadataToInputs(r, m)
	if err != nil {
		t.Fatalf("metadataToInputs: %v", err)
	}

	if res[0].Image != nil {
		t.Fatalf("expected image to be nil")
	}
}

func TestMetadataBrokenImage(t *testing.T) {
	body := new(bytes.Buffer)
	w := multipart.NewWriter(body)

	WithFile(t, w, "testfiles/broken.png", "anjay")

	err := w.Close()
	if err != nil {
		t.Fatalf("w close: %v", err)
	}

	r, err := http.NewRequest(http.MethodPost, "/sss", body)
	r.Header.Add("Content-Type", w.FormDataContentType())

	if err != nil {
		t.Fatalf("req: %v", err)
	}

	m := allMetadata{
		[4]metadata{
			{
				Name:        "suki",
				Description: "sukite yo",
				ImgKey:      "anjay",
			},
		},
	}

	_, err = metadataToInputs(r, m)
	if err == nil {
		t.Fatalf("expected error")
	}

	if !errors.Is(err, errInvalidImageType) {
		t.Fatalf("expected error: %v, got: %v", errInvalidImageType, err)
	}
}

func WithFile(t *testing.T, mw *multipart.Writer, filePath, key string) {
	t.Helper()

	file, err := os.Open(filePath)
	if err != nil {
		t.Fatalf("failed: %v", err)
	}

	defer func() {
		err = file.Close()
		if err != nil {
			t.Fatalf("close file: %v", err)
		}
	}()

	fs := filepath.Base(filePath)

	part, err := mw.CreateFormFile(key, fs)
	if err != nil {
		t.Fatalf("create form file: %v", err)
	}

	_, err = io.Copy(part, file)
	if err != nil {
		t.Fatalf("io copy: %v", err)
	}
}
