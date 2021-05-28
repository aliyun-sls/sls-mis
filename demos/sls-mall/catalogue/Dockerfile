FROM golang:1.16.4

ENV OTLP_ENDPOINT="" \
    MYSQL_USER="root" \
    MYSQL_HOST="mysql" \
    MYSQL_PASSWORD="root" \
    MYSQL_DATABASE="socksdb"

WORKDIR /sls-mall

COPY app .
COPY images ./images
COPY startup.sh .
RUN chmod +x /sls-mall/app startup.sh
CMD ["/sls-mall/startup.sh"]