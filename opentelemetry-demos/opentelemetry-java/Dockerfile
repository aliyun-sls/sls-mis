FROM openjdk:11-jdk-oracle
ENV DEPLOY_HOME="/opentelemetry" \
    ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    HTTPS_ENDPOINT="" \
    SERVICE_NAME="opentelementry-java" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"
COPY ./docker/startup.sh /opentelemetry/
COPY ./target/opentelemetry-javaagent-all.jar  /opentelemetry/
COPY ./target/opentelemetry-java.jar  /opentelemetry/

WORKDIR /opentelemetry
EXPOSE 8085
CMD ["sh", "/opentelemetry/startup.sh"]
