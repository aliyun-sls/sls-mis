FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="shipping" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /sls-mall
COPY target/shipping.jar ./app.jar
COPY target/opentelemetry-javaagent-all.jar .
COPY startup.sh .
RUN chmod +x startup.sh
EXPOSE 8088
CMD ["/sls-mall/startup.sh"]
