FROM openjdk:11-jdk-oracle
ENV DEPLOY_HOME="/opentelemetry" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    HTTPS_ENDPOINT="" \
    SERVICE_NAME="opentelementry-java" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1" \
    SERVICE_CONFIG_PATH=""

WORKDIR /opentelemetry

COPY ./docker/startup.sh /opentelemetry/
COPY ./target/opentelemetry-javaagent-all.jar  /opentelemetry/
COPY ./target/opentelemetry-java.jar  /opentelemetry/

EXPOSE 8085
CMD ["sh", "/opentelemetry/startup.sh"]
