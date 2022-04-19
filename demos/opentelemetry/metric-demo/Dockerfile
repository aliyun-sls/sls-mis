FROM openjdk:8
WORKDIR /metric-demo
ENV PROJECT="" \
    INSTANCE="" \
    ACCESS_KEY="" \
    ACCESS_SECRITY=""  \
    ENDPOINT=""

COPY target/metric-demo.jar .

CMD ["java", "-jar", "/metric-demo/metric-demo.jar"]