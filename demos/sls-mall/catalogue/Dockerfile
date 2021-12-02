FROM golang:1.16.4

ENV OTLP_ENDPOINT="" \
    MYSQL_USER="root" \
    MYSQL_HOST="mysql" \
    MYSQL_PASSWORD="root" \
    MYSQL_DATABASE="socksdb" \
    DEPLOYMENT_ENVIRONMENT="production" \
    SERVICE_VERSION="1.3.0"

LABEL "app"="catalogue"
LABEL "demo"="sls-mall"
LABEL "language"="go"

WORKDIR /sls-mall

COPY app .
COPY images ./images
COPY startup.sh .
RUN chmod +x /sls-mall/app startup.sh
CMD ["/sls-mall/startup.sh"]