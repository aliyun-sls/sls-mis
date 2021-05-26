FROM openjdk:8-alpine
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT=""

WORKDIR /sls-mall
COPY target/shipping.jar ./app.jar
COPY target/opentelemetry-javaagent-all.jar .
COPY startup.sh .
RUN chmod +x startup.sh
CMD ["/sls-mall/startup.sh"]
