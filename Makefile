REGION = asia-east1

run:
	functions-framework --target=signup

deploy:
	gcloud functions deploy signup_fn \
	--gen2 \
	--runtime=python311 \
	--region=$(REGION) \
	--trigger-location=$(REGION) \
	--source=. \
	--entry-point=signup \
	--timeout=540 \
