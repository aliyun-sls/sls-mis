FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="orders" \
    SERVICE_VERSION="1.1.0" \
    DEPLOYMENT_ENVIRONMENT="production" \
   CLIENT_MAX_SLEEP_TIME_US=0 \
    CLIENT_MIN_SLEEP_TIME_US=0 \
    SLOW_P=0 \
    SERVER_MIN_SLEEP_TIME_US=0 \
    THROW_EXCEPTION_P=0 \
    SERVER_MAX_SLEEP_TIME_US=0

WORKDIR /sls-mall
COPY target/orders.jar ./app.jar
COPY agents/opentelemetry-javaagent-all.jar .
COPY agent/jolokia-jvm-1.7.1.jar .
COPY agent/jmx_prometheus_javaagent-0.16.1.jar .
COPY config.yaml .
COPY startup.sh .
RUN chmod +x startup.sh
EXPOSE 8086
CMD ["/sls-mall/startup.sh"]
