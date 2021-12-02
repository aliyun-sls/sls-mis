FROM golang:1.16.4

ENV OTLP_ENDPOINT="" \
    DEPLOYMENT_ENVIRONMENT="production" \
    SERVICE_VERSION="1.3.0"

WORKDIR /sls-mall
LABEL "app"="payment"
LABEL "demo"="sls-mall"
LABEL "language"="go"

COPY app .
RUN chmod +x /sls-mall/app
CMD ["/sls-mall/app"]