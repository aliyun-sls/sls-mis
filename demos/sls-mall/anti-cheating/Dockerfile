FROM openjdk:8u252-jdk
ENV PROJECT="" \
    ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="anti-cheating" \
    SERVICE_VERSION="1.1.0" \
    DATABASE_HOST="integral-db" \
    DATABASE_NAME="integral" \
    DATABASE_USER="root" \
    DATABASE_PASSWORD="" \
    DEPLOYMENT_ENVIRONMENT="production" 

WORKDIR /sls-mall
COPY target/anti-cheating.jar ./app.jar
COPY target/opentelemetry-javaagent-all.jar .
COPY startup.sh .
RUN chmod +x startup.sh
EXPOSE 8090
CMD ["/sls-mall/startup.sh"]
