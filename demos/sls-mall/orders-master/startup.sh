#!/usr/bin/env sh
set -x
export SERVICE_HOST=`hostname`
java -javaagent:/sls-mall/jolokia-jvm-1.7.1.jar=port=7777,host=0.0.0.0 -javaagent:/sls-mall/opentelemetry-javaagent-all.jar -Dotel.exporter.otlp.endpoint=${ENDPOINT} \
  -Dotel.exporter.otlp.compression=gzip -Dotel.exporter.otlp.protocol=grpc \
  -Dotel.exporter.otlp.headers=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET} \
  -Dotel.resource.attributes=deployment.environment=${DEPLOYMENT_ENVIRONMENT},service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar app.jar