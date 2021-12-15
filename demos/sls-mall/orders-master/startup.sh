#!/usr/bin/env sh
set -x
export SERVICE_HOST=`hostname`
java -javaagent:/sls-mall/jmx_prometheus_javaagent-0.16.1.jar=8888:config.yaml -javaagent:/sls-mall/jolokia-jvm-1.7.1.jar=port=7777,host=0.0.0.0 -javaagent:/sls-mall/opentelemetry-javaagent-all.jar -Dotel.exporter.otlp.endpoint=${ENDPOINT} \
  -Dotel.exporter.otlp.compression=gzip -Dotel.exporter.otlp.protocol=grpc \
  -Dotel.exporter.otlp.headers=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET} \
  -Dotel.resource.attributes=k8s.pod.name=${POD_NAME},k8s.namespace=${POD_NAMESPACE},k8s.node=${NODE_NAME},deployment.environment=${DEPLOYMENT_ENVIRONMENT},service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar app.jar