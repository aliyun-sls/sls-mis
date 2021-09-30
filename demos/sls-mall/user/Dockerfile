FROM golang:1.7-alpine
ENV MONGO_HOST=mongo:27017 \
    HATEAOS=user \
    USER_DATABASE=mongodb \
    ZIPKIN="" \
    DEPLOYMENT_ENVIRONMENT="production" \
    SERVICE_VERSION="1.1.0"

WORKDIR /sls-mall
LABEL "demo"="sls-mall"
LABEL "app"="user"
LABEL "language"="go"
COPY app .
EXPOSE 8084
CMD ["/sls-mall/app"]