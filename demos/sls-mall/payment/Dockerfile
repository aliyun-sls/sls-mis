FROM golang:1.16.4

ENV OTLP_ENDPOINT="" \
    DEPLOYMENT_ENVIRONMENT="production" \
    SERVICE_VERSION="1.1.0"

WORKDIR /sls-mall

COPY app .
RUN chmod +x /sls-mall/app
CMD ["/sls-mall/app"]