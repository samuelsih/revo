package infra

import (
	"context"
	"fmt"
	"io"
	"log/slog"

	"cloud.google.com/go/storage"
)

const cacheControl = "Cache-Control:no-cache, max-age=0"

type UploaderFunc func(ctx context.Context, objectName string, file io.Reader) (string, error)

func (u UploaderFunc) Upload(ctx context.Context, objectName string, file io.Reader) (string, error) {
	return u(ctx, objectName, file)
}

func Uploader(client *storage.Client, bucketName string) UploaderFunc {
	bucket := client.Bucket(bucketName)

	return func(ctx context.Context, objectName string, file io.Reader) (string, error) {
		wc := bucket.Object(objectName).NewWriter(ctx)
		wc.ObjectAttrs.CacheControl = cacheControl

		_, err := io.Copy(wc, file)
		if err != nil {
			return "", err
		}

		err = wc.Close()
		if err != nil {
			slog.Warn("closing gcs writer failed: %v", err)
			return "", err
		}

		return fmt.Sprintf("https://storage.googleapis.com/%s/%s", bucketName, objectName), nil
	}
}
