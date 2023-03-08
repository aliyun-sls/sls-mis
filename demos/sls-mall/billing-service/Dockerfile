FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="billing" \
    SERVICE_VERSION="1.0.0" \
    DEPLOYMENT_ENVIRONMENT="production" \
    ROCKETMQ_ACCESSKEY="" \
    ROCKETMQ_SECRETKEY="" \
    ROCKETMQ_ENDPOINTS="" \
    ROCKETMQ_ORDER_TOPIC=""

WORKDIR /sls-mall
COPY target/billing.jar ./app.jar
COPY agents/opentelemetry-javaagent.jar .
COPY startup.sh .
RUN chmod +x startup.sh

EXPOSE 8080
CMD ["/sls-mall/startup.sh"]
