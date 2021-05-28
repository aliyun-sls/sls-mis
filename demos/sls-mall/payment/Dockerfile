FROM golang:1.16.4

ENV OTLP_ENDPOINT=""

WORKDIR /sls-mall

COPY app .
RUN chmod +x /sls-mall/app
CMD ["/sls-mall/app"]