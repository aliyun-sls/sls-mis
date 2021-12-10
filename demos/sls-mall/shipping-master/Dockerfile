FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="shipping" \
    SERVICE_VERSION="1.1.0" \
    DEPLOYMENT_ENVIRONMENT="production"

WORKDIR /sls-mall
COPY target/shipping.jar ./app.jar
COPY agents/opentelemetry-javaagent-all.jar .
COPY agent/jolokia-jvm-1.7.1.jar .
COPY agent/jmx_prometheus_javaagent-0.16.1.jar .
COPY config.yaml .
COPY startup.sh .
RUN chmod +x startup.sh
EXPOSE 8088
CMD ["/sls-mall/startup.sh"]
