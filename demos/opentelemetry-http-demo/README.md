# OpenTelemetry Http Demo


## Quick Start

1. 生成jar包。该阶段会下载OpenTelemetry javaagent包，整体时间会比较长。

```bash
$ mvn clean package
```

2. 启动服务
```bash
export PROJECT=<PROJECT_NAME> 
export INSTANCE=<INSTANCE>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export OTEL_EXPORTER_OTLP_ENDPOINT=${ENDPOINT}
export SERVICE_NAME=opentelemetry-http-demo
export SERVICE_VERSION=v1.0.0
export SERVICE_HOST=127.0.0.1
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_HEADERS=x-sls-otel-project=${PROJECT},x-sls-otel-instance-id=${INSTANCE},x-sls-otel-ak-id=${ACCESS_KEY_ID},x-sls-otel-ak-secret=${ACCESS_SECRET}
export SERVICE_NAMESPACE=opentelemetry-http-demo

java -javaagent:$PWD/agent/opentelemetry-javaagent.jar -Dotel.resource.attributes=service.name=${SERVICE_NAME},service.version=${SERVICE_VERSION},host.name=${SERVICE_HOST},service.namespace=${SERVICE_NAMESPACE} -jar $PWD/target/opentelemetry-http-demo.jar
```

各参数详细介绍:

|参数名| 参数描述                                                                                                                                                                             |
|:---|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。                                                                                 |
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。                                                                                                              |
|PROJECT_NAME| 日志服务Project名称。                                                                                                                                                                   |
|INSTANCE| Trace服务实例名称。                                                                                                                                                                     |
|ENDPOINT| 接入地址，格式为http(s)://${project}.${region-endpoint}/opentelemetry，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。<br/> 协议支持 http 和 https |
|SERVICE_NAME| 服务名                                                                                                                                                                              |
|SERVICE_VERSION| 服务版本号。建议按照va.b.c格式定义。                                                                                                                                                            |

3. 访问服务

```shell
curl http://localhost:8080/sayHello
```
