FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="orders" \
    SERVICE_VERSION="1.1.0" \
    DEPLOYMENT_ENVIRONMENT="production" 

WORKDIR /sls-mall
COPY target/orders.jar ./app.jar
COPY agents/opentelemetry-javaagent-all.jar .
COPY agents/jolokia-jvm-1.7.1.jar .
COPY agents/jmx_prometheus_javaagent-0.16.1.jar .
COPY config.yaml .
COPY startup.sh .
RUN chmod +x startup.sh && apt-get update && apt-get -y install stress-ng

EXPOSE 8086
CMD ["/sls-mall/startup.sh"]
