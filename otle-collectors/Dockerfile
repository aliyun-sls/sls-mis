FROM otel/opentelemetry-collector-contrib:0.24.0 AS BUILD
FROM alpine:latest
ENV ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    ENDPOINT="" \
    LOGSTORE=""

COPY --from=BUILD /otelcontribcol /

ARG VENDOR=opentelemetry
copy config-${VENDOR}.yaml /etc/otel/config.yaml
COPY entrypoint.sh /entrypoint.sh
COPY --from=BUILD /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt
RUN chmod +x /entrypoint.sh
EXPOSE 6850 6831 6832 6833 6834 55680 55681 9411
ENTRYPOINT ["/entrypoint.sh"]
CMD ["/otelcontribcol", "--config=/etc/otel/config.yaml"]
