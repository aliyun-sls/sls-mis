FROM openjdk:11-jdk-oracle

ENV PROJECT=PROJECT_NAME \
    INSTANCE=INSTANCE_NAME \
    HTTPS_ENDPOINT=HTTPS_ENDPOINT \
    ACCESS_KEY_ID=ACCESS_KEY_ID \
    ACCESS_SECRET=ACCESS_SECRET \
    SERVICE_NAME="projectB" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /trace-with-log-demo

COPY agents/opentelemetry-javaagent.jar .
COPY target/projectB.jar .
COPY entrypoint.sh /
COPY startup.sh .
RUN chmod +x /entrypoint.sh & chmod +x /trace-with-log-demo/startup.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["/trace-with-log-demo/startup.sh"]