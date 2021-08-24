#!/usr/bin/env sh
set -x
java -javaagent:/sls-mall/opentelemetry-javaagent-all.jar -Dotel.exporter.otlp.endpoint=${ENDPOINT} \
  -Dotel.exporter.otlp.compression=gzip -Dotel.exporter.otlp.protocol=grpc \
  -Dotel.exporter.otlp.headers=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET} \
  -Dotel.resource.attributes=${DEPLOYMENT_ENVIRONMENT},=production,service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar app.jar