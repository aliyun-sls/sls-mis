# Apache Dubbo Demo

## Quick Start

1. 构建源码

```shell
./mvnw clean package
```

2. 启动Provider端

```shell
export PROJECT=<PROJECT_NAME>
export INSTANCE=<INSTANCE>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export OTEL_EXPORTER_OTLP_ENDPOINT=${ENDPOINT}
export SERVICE_NAME=apache-dubbo-provider-demo
export SERVICE_VERSION=v1.0.0
export SERVICE_HOST=127.0.0.1
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
export OTEL_EXPORTER_OTLP_COMPRESSION=gzip
export OTEL_EXPORTER_OTLP_HEADERS=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET}
java -javaagent:$PWD/agent/opentelemetry-javaagent-all.jar -Dotel.resource.attributes=service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar $PWD/dubbo-provider/target/dubbo-provider-demo.jar
```

各参数详细介绍

|参数名|参数描述|
|:---|:---|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。|
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。|
|PROJECT_NAME|日志服务Project名称。 |
|INSTANCE|Trace服务实例名称。 |
|ENDPOINT|接入地址，格式为https://${project}.${region-endpoint}:10010，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。 |
|SERVICE_NAME|服务名|
|SERVICE_VERSION|服务版本号。建议按照va.b.c格式定义。|

3. 启动Consumer端

打开一个新的Terminal，执行下面命令, 各参数详细参见Provider端章节

```shell
export PROJECT=<PROJECT_NAME>
export INSTANCE=<INSTANCE>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export OTEL_EXPORTER_OTLP_ENDPOINT=${ENDPOINT}
export SERVICE_NAME=apache-dubbo-consumer-demo
export SERVICE_VERSION=v1.0.0
export SERVICE_HOST=127.0.0.1
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
export OTEL_EXPORTER_OTLP_COMPRESSION=gzip
export OTEL_EXPORTER_OTLP_HEADERS=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET}
java -javaagent:$PWD/agent/opentelemetry-javaagent-all.jar -Dotel.resource.attributes=service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST} -jar $PWD/dubbo-consumer/target/dubbo-consumer-demo.jar
```

4. 访问服务

```shell
curl http://localhost:8091/sayHello
```