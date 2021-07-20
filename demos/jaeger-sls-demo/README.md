# Jaeger SLS Demo

## Quick start

1. 构建工程
```shell
go build
```

2. 启动Demo
```shell
export PROJECT=<PROJECT_NAME>
export ENDPOINT=<ENDPOINT>
export INSTANCE=<TRACE_INSTANCE_ID>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export SERVICE_NAME=opentelemetry-go

./jaeger-sls-demo
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