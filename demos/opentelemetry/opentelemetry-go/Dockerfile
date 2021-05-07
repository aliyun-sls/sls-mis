FROM golang
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-go-0.16.0" \
    SERVICE_VERSION="0.16.0" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /opentelemetry-go

COPY main /opentelemetry-go/
RUN chmod +x main

CMD ["/opentelemetry-go/main"]
