FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="integral" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1" \
    DATABASE_HOST="integral-db" \
    DATABASE_NAME="integral" \
    DATABASE_USER="root" \
    DATABASE_PASSWORD="" \
    DEPLOYMENT_ENVIRONMENT="production"

WORKDIR /sls-mall
COPY target/integral.jar ./app.jar
COPY target/opentelemetry-javaagent-all.jar .
COPY startup.sh .
RUN chmod +x startup.sh
EXPOSE 8091
CMD ["/sls-mall/startup.sh"]
