FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="orders" \
    SERVICE_VERSION="1.1.0" \
    DEPLOYMENT_ENVIRONMENT="production" \
    ROCKETMQ_ACCESSKEY="" \
    ROCKETMQ_SECRETKEY="" \
    ROCKETMQ_ENDPOINTS="" \
    ROCKETMQ_ORDER_TOPIC=""

WORKDIR /sls-mall
COPY target/orders.jar ./app.jar
COPY agents/opentelemetry-javaagent.jar .
COPY config.yaml .
COPY startup.sh .
RUN chmod +x startup.sh && apt-get update && apt-get -y install stress-ng

EXPOSE 8086
CMD ["/sls-mall/startup.sh"]
