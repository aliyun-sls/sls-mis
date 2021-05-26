# Opentelemetry Go Demo

Opentelemetry Go Demo 采用 Opentelemetry Go 手动探针采集 Trace 数据，并发送给 SLS 服务

## Quick Start

1. 构建应用

```shell
export PROJECT=<PROJECT_NAME>
export ENDPOINT=<ENDPOINT>
export INSTANCE=<TRACE_INSTANCE_ID>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export SERVICE_NAME=opentelemetry-go
export SERVICE_VERSION=v1.0.0
export SERVICE_HOST=127.0.0.1

go build
```

各参数详细介绍:

|参数名|参数描述|
|:---|:---|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。|
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。|
|PROJECT_NAME|日志服务Project名称。 |
|INSTANCE|Trace服务实例名称。 |
|ENDPOINT|接入地址，格式为${project}.${region-endpoint}:10010，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。 |
|SERVICE_NAME|服务名|
|SERVICE_VERSION|服务版本号。建议按照va.b.c格式定义。|


2. 启动服务

```shell
./opentelemetry-go-demo
```

3. 访问服务

```shell
curl http://localhost:8084/hello-world
curl http://localhost:8084/save-order\?orderID=123
curl http://localhost:8084/save-order\?orderID=123x
```
