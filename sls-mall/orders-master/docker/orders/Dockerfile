FROM weaveworksdemos/msd-java:jre-latest

WORKDIR /usr/src/app
COPY *ers.jar ./app.jar
COPY opentelemetry-javaagent-all.jar ./opentelemetry-javaagent.jar

RUN	chown -R ${SERVICE_USER}:${SERVICE_GROUP} ./app.jar

USER ${SERVICE_USER}

ENTRYPOINT ["/usr/local/bin/java.sh","-jar","./app.jar", "--port=80"]
