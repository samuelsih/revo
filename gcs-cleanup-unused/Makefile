local:
	set FUNCTION_TARGET=cleanup && php -S localhost:8080 vendor/bin/router.php    

deploy:
	gcloud functions deploy destroy-unused-pic-gcs \
	--gen2 \
	--region=asia-southeast2 \
	--runtime=php82 \
	--source=. \
	--entry-point=cleanup \
	--trigger-topic=cleanup-gcs \
	--set-env-vars PROJECT_ID=$(PROJECT_ID),BUCKET_NAME=$(BUCKET_NAME)