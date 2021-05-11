# Opentelemetry Java Demo

本工程使用 opentelemetry-java 自动探针采集 Trace 数据，并通过 OTLP 协议发送到 SLS

## 依赖组件

- JDK 1.8u252+

## 快速启动

1. 构建应用

```shell
./mvnw clean package
```

2. 启动服务

```shell
export PROJECT=<PROJECT_NAME>
export INSTANCE_ID=<INSTANCE_ID>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export OTEL_EXPORTER_OTLP_ENDPOINT=${ENDPOINT}
export SERVICE_NAME=opentelemetry-java
export SERVICE_VERSION=1.0.0
export SERVICE_HOST=127.0.0.1
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
export OTEL_EXPORTER_OTLP_COMPRESSION=gzip
export OTEL_EXPORTER_OTLP_HEADERS=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE_ID},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET}
java -javaagent:$PWD/target/opentelemetry-javaagent-all.jar -Dotel.resource.attributes=service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar $PWD/target/opentelemetry-java.jar
```

3. 访问服务

```shell
curl http://localhost:8085/hello-world
```
