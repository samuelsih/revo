package main

import (
	"context"
	"flag"
	"log/slog"
	"net/http"
	"os"
	"time"

	"cloud.google.com/go/pubsub"
	"cloud.google.com/go/storage"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/jackc/pgx/v5"
	"github.com/joho/godotenv"
	"github.com/samuelsih/revo-voting/infra"
)

type AppConfig struct {
	ProjectID   string `env:"PROJECT_ID"`
	PubSubTopic string `env:"PUBSUB_TOPIC"`
	BucketName  string `env:"BUCKET_NAME"`
	Port        string `env:"PORT" default:"8080"`
	DBURL       string `env:"DB_URL"`
}

var appConfig AppConfig

func init() {
	var isProd bool

	flag.BoolVar(&isProd, "prod", true, "set development status")

	if !isProd {
		slog.Info("Running on dev mode")

		if err := godotenv.Load(".env"); err != nil {
			slog.Error("cannot get env: %v", err)
			os.Exit(1)
		}
	}

	if err := FillEnv(&appConfig); err != nil {
		slog.Error("cannot fill env config: %v", err)
		os.Exit(1)
	}
}

func main() {
	r := chi.NewRouter()
	ctx := context.Background()

	pubsubClient, err := pubsub.NewClient(ctx, appConfig.ProjectID)
	if err != nil {
		slog.Error("failed create pubsub client: %v", err)
		os.Exit(1)
	}

	storageCLient, err := storage.NewClient(ctx)
	if err != nil {
		slog.Error("failed create storage client: %v", err)
		os.Exit(1)
	}

	pg, err := pgx.Connect(ctx, appConfig.DBURL)
	if err != nil {
		slog.Error("failed create db client: %v", err)
		os.Exit(1)
	}

	defer pg.Close(ctx)

	err = pg.Ping(ctx)
	if err != nil {
		slog.Error("failed ping pg client: %v", err)
		os.Exit(1)
	}

	t := pubsubClient.Topic(appConfig.PubSubTopic)

	publisher := infra.Publisher(t)
	uploader := infra.Uploader(storageCLient, appConfig.BucketName)
	persister := infra.SaveVotingTheme(pg)

	deps := Dependencies{
		Publisher:        publisher,
		Uploader:         uploader,
		VotingThemeSaver: persister,
	}

	r.Use(
		middleware.Logger,
		middleware.Recoverer,
		GetUserFromHeader,
	)

	r.Post("/voting", CreateVotingThemeHandler(deps))

	server := &http.Server{
		Addr:         ":" + appConfig.Port,
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
	}

	slog.Info("Server Start")

	err = server.ListenAndServe()
	if err != nil {
		slog.Error("cant serve: %v", err)
		os.Exit(1)
	}
}
