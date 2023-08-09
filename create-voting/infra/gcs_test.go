package infra

import (
	"context"
	"os"
	"testing"

	"cloud.google.com/go/storage"
)

var fakeGCS *storage.Client

func TestGCS(t *testing.T) {
	t.Parallel()

	uploader := Uploader(fakeGCS, "some-bucket")

	file, err := os.Open(".gitignore")
	if err != nil {
		t.Errorf("Cannot read file: %v", err)
	}

	defer file.Close()

	result, err := uploader.Upload(context.TODO(), "gitignore", file)
	if err != nil {
		t.Errorf("Upload failed: %v", err)
	}

	expected := "https://storage.googleapis.com/some-bucket/gitignore"

	if result != expected {
		t.Errorf("Expected %s, got %s", expected, result)
	}
}
