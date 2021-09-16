FROM openjdk:11-jdk-oracle

ENV PROJECT="" \
    INSTANCE="" \
    HTTPS_ENDPOINT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    SERVICE_NAME="backend" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /trace-log-demos

COPY agents/opentelemetry-javaagent.jar .
COPY target/backend.jar .
COPY startup.sh .
COPY entrypoint.sh .
RUN chmod +x /trace-log-demos/*.sh

ENTRYPOINT ["/trace-log-demos/entrypoint.sh"]
CMD ["/trace-log-demos/startup.sh"]