package infra

import (
	"context"
	"log/slog"

	"cloud.google.com/go/pubsub"
)

type PublisherFunc func(ctx context.Context, msg []byte)

func (p PublisherFunc) Publish(ctx context.Context, msg []byte) {
	p(ctx, msg)
}

func Publisher(t *pubsub.Topic) PublisherFunc {
	return func(ctx context.Context, msg []byte) {
		res := t.Publish(ctx, &pubsub.Message{Data: msg})

		go func() {
			_, err := res.Get(ctx)
			if err != nil {
				slog.Warn("publish failed: %v", err)
			}
		}()
	}
}
