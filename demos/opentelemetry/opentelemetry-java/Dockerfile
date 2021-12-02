FROM openjdk:11-jdk-oracle
ENV DEPLOY_HOME="/opentelemetry" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-java" \
    SERVICE_VERSION="v2.0.0" \
    SERVICE_HOST="127.0.0.1" \
    SERVICE_NAMESPACE="opentelemetry-demo" \
    SERVICE_CONFIG_PATH=""

WORKDIR /opentelemetry

COPY ./docker/startup.sh /opentelemetry/
COPY ./target/opentelemetry-javaagent.jar  /opentelemetry/
COPY ./target/opentelemetry-java.jar  /opentelemetry/

EXPOSE 8085
CMD ["sh", "/opentelemetry/startup.sh"]
