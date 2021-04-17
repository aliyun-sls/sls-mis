FROM golang
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-go-demo" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /opentelemetry-go

COPY main /opentelemetry-go/
RUN chmod +x main

CMD ["/opentelemetry-go/main"]
