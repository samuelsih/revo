run:
	uvicorn main:app --reload

build:
	docker build -t revo-auth:1.0.0 .

up:
	docker run -d -p 8000:8000 -e PORT=8000 --name revo-auth-container revo-auth:1.0.0