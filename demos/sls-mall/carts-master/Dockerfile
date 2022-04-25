FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="carts" \
    SERVICE_VERSION="1.1.0" \
    DEPLOYMENT_ENVIRONMENT="production" \
    CLIENT_MAX_SLEEP_TIME_US=0 \
    CLIENT_MIN_SLEEP_TIME_US=0 \
    SLOW_P=0 \
    SERVER_MIN_SLEEP_TIME_US=0 \
    THROW_EXCEPTION_P=0 \
    SERVER_MAX_SLEEP_TIME_US=0

WORKDIR /sls-mall
COPY target/carts.jar ./app.jar
COPY target/opentelemetry-javaagent-all.jar .
COPY startup.sh .
RUN apt-get update && apt-get -y install stress-ng && chmod +x startup.sh
EXPOSE 8089
CMD ["/sls-mall/startup.sh"]
