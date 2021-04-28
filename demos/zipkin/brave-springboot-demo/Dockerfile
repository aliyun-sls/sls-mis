FROM openjdk:11-jdk-oracle
ENV OTPL_COLLECTOR_ADDRESS="localhost:9411" \
    SERVICE_NAME="brave-springboot-demo" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1" \
    SERVICE_CONFIG_PATH="/services"

WORKDIR /brave-springboot-demo

COPY ./target/brave-springboot-demo.jar ./

CMD ["java", "-Dservice.path=${SERVICE_CONFIG_PATH}", "-Dzipkin.baseUrl=${OTPL_COLLECTOR_ADDRESS}","-Dspring.application.name=${SERVICE_NAME}", "-jar", "./brave-springboot-demo.jar"]
