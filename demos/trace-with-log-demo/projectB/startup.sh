#!/usr/bin/env sh
set -x
java -javaagent:/trace-with-log-demo/opentelemetry-javaagent.jar -Dotel.exporter.otlp.endpoint=${HTTPS_ENDPOINT} \
  -Dotel.exporter.otlp.compression=gzip -Dotel.exporter.otlp.protocol=grpc \
  -Dotel.exporter.otlp.headers=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET} \
  -Dotel.resource.attributes=service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar projectB.jar