FROM python:3.10.12-alpine

ENV PYTHONUNBUFFERED True
ENV ENVIRONMENT PRODUCTION

WORKDIR /app

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

CMD exec uvicorn main:app --host 0.0.0.0 --port ${PORT} --workers 3