#!/usr/bin/env sh
set -x
export SERVICE_HOST=`hostname`

export OTEL_EXPORTER_OTLP_COMPRESSION=gzip
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
export OTEL_RESOURCE_ATTRIBUTES=deployment.environment=${DEPLOYMENT_ENVIRONMENT},service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST}
export OTEL_EXPORTER_OTLP_HEADERS=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET}

java -javaagent:/sls-mall/opentelemetry-javaagent-all.jar -Dotel.java.disabled.resource.providers=io.opentelemetry.sdk.extension.resources.ProcessResourceProvider,io.opentelemetry.sdk.extension.resources.ProcessRuntimeResourceProvider -Dotel.exporter.otlp.endpoint=${ENDPOINT} -jar app.jar