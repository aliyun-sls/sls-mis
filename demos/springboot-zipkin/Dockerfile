FROM openjdk:8

WORKDIR /springboot-zipkin

ENV PROJECT="" \
    INSTANCE="" \
    ACCESS_KEY="" \
    ACCESS_SECRITY=""  \
    ZIPKIN_ENDPOINT=""

COPY target/springboot-zipkin.jar .
COPY startup.sh .

EXPOSE 8080
CMD ["sh", "startup.sh"]