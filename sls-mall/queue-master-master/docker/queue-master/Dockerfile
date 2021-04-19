FROM weaveworksdemos/msd-java:jre-latest

WORKDIR /usr/src/app
COPY *master.jar ./app.jar
COPY opentelemetry-javaagent-all.jar ./opentelemetry-javaagent.jar

ENTRYPOINT ["/usr/local/bin/java.sh","-jar","./app.jar", "--port=80"]
