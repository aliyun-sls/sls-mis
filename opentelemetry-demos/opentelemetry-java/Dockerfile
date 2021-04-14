FROM openjdk:12-jdk-alpine3.9
ENV DEPLOY_HOME="/opentelemetry" \
    ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-java-demo" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"

COPY ./docker/startup.sh /opentelemetry/
COPY ./target/opentelemetry-javaagent-all.jar  /opentelemetry/
COPY ./target/opentelemetry-java.jar  /opentelemetry/

WORKDIR /opentelemetry
EXPOSE 8080
CMD ["sh", "/opentelemetry/startup.sh"]