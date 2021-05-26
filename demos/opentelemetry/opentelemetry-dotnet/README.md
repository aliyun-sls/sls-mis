# Opentelemetry Dotnet demo

Opentelemetry Dotnet demo 通过 Opentelemetry-Dotnet 自动埋点探针采集 Trace 数据，并将 Trace 数据上传 SLS 服务中。

## 依赖

- dotnet 5.0

## 快速

1. 构建 NuGet 依赖

```shell
dotnet restore
```

2. 启动应用

```shell
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export PROJECT=<PROJECT_NAME>
export INSTANCE=<LOGSTORE_NAME>
export ENDPOINT=<ENDPOINT>
export SERVICE_NAME=opentelemetry-dotnet
export SERVICE_VERSION=v1.0.0
export SERVICE_HOST=127.0.0.1

dotnet run --project WebApplication
```

各参数详细介绍:

|参数名|参数描述|
|:---|:---|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。|
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。|
|PROJECT_NAME|日志服务Project名称。 |
|INSTANCE|Trace服务实例名称。 |
|ENDPOINT|接入地址，格式为https://${project}.${region-endpoint}:10010，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。 |
|SERVICE_NAME|服务名|
|SERVICE_VERSION|服务版本号。建议按照va.b.c格式定义。|

3. 访问服务

```shell
curl http://localhost:8083/hello-world
```
