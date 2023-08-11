package infra

import (
	"context"
	"fmt"
	"log"
	"os"
	"testing"

	"github.com/fsouza/fake-gcs-server/fakestorage"
	"github.com/jackc/pgx/v5"
	"github.com/ory/dockertest/v3"
	"github.com/ory/dockertest/v3/docker"
)

func TestMain(m *testing.M) {
	ctx := context.Background()
	pool, err := dockertest.NewPool("")
	if err != nil {
		log.Fatalf("Could not construct pool: %s", err)
	}

	err = pool.Client.Ping()
	if err != nil {
		log.Fatalf("Could not connect to Docker: %s", err)
	}

	pgResource := startPostgresDB(ctx, pool)
	setupGCSFakeServer()

	code := m.Run()

	err = fakeGCS.Close()
	if err != nil {
		log.Fatalf("Could not close fake gcs: %v", err)
	}

	err = cleanup(pool, pgResource)
	if err != nil {
		log.Fatalf("Could not purge resource: %v", err)
	}

	os.Exit(code)
}

func startPostgresDB(ctx context.Context, pool *dockertest.Pool) *dockertest.Resource {
	resource, err := pool.RunWithOptions(&dockertest.RunOptions{
		Repository: "postgres",
		Tag:        "12",
		Env: []string{
			"POSTGRES_PASSWORD=secret",
			"POSTGRES_USER=user_name",
			"POSTGRES_DB=dbname",
			"listen_addresses='*'",
		},
	}, func(config *docker.HostConfig) {
		config.AutoRemove = true
		config.RestartPolicy = docker.RestartPolicy{Name: "no"}
	})
	if err != nil {
		log.Fatalf("Could not start resource: %s", err)
	}

	hostAndPort := resource.GetHostPort("5432/tcp")
	databaseURL := fmt.Sprintf("postgres://user_name:secret@%s/dbname?sslmode=disable", hostAndPort)

	log.Println("Connecting to database on url: ", databaseURL)

	err = resource.Expire(120)
	if err != nil {
		log.Fatalf("Could not expire the resource: %v", err)
	}

	if err = pool.Retry(func() error {
		dbTest, err = pgx.Connect(context.Background(), databaseURL)
		if err != nil {
			return err
		}
		return dbTest.Ping(ctx)
	}); err != nil {
		log.Fatalf("Could not connect to docker: %s", err)
	}

	_, err = dbTest.Exec(ctx, `create table voting_theme (
		id character varying not null,
		user_id character varying not null,
		start_at timestamp with time zone not null,
		end_at timestamp with time zone not null,
		metadata jsonb not null,
		created_at timestamp with time zone null default (now() at time zone 'utc'::text),
		constraint voting_theme_pkey primary key (id)
		) tablespace pg_default;`,
	)

	if err != nil {
		log.Fatalf("Could not create table: %v", err)
	}

	_, err = dbTest.Exec(ctx, `INSERT INTO
		voting_theme (id, user_id, start_at, end_at, metadata)
		VALUES
			(
			'some-id',
			'user',
			'2023-08-09 13:45:55.330487+00',
			'2023-08-09 18:45:55.330487+00',
			'[{"name":"sdsdsdsds","img_link":"https://storage.googleapis.com/","description":"dsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsd"}]'
			)
		;`,
	)

	if err != nil {
		log.Fatalf("Could not insert to table: %v", err)
	}

	return resource
}

func setupGCSFakeServer() {
	server := fakestorage.NewServer([]fakestorage.Object{
		{
			ObjectAttrs: fakestorage.ObjectAttrs{
				BucketName: "some-bucket",
				Name:       "some/object/file.txt",
			},
			Content: []byte("inside the file"),
		},
	})

	fakeGCS = server.Client()
}

func cleanup(pool *dockertest.Pool, resources ...*dockertest.Resource) error {
	for _, resource := range resources {
		if err := pool.Purge(resource); err != nil {
			return err
		}
	}

	return nil
}
