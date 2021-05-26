# Opentelemetry PHP demo

Opentelemetry PHP demo 使用 Opentelemetry-PHP 手动埋点采集 Trace，并上传到 SLS

## 依赖组件

- php
- composer

## Quick start

1. 下载依赖

```shell
composer install
```

2. 启动服务

```shell
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export PROJECT=<PROJECT_NAME>
export INSTANCE=<INSTANCE>
export ZIPKIN_ENDPOINT=<ZIPKIN_ENDPOINT>
export SERVICE_NAME=opentelemetry-php
export SERVICE_VERSION=v1.0.0

php -S '0.0.0.0:8087' src/server.php
```

各参数详细介绍:

|参数名|参数描述|
|:---|:---|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。|
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。|
|PROJECT_NAME|日志服务Project名称。 |
|INSTANCE|Trace服务实例名称。 |
|ZIPKIN_ENDPOINT|接入地址，格式为https://${project}.${region-endpoint}/zipkin/api/v2/spans，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。 |
|SERVICE_NAME|服务名|
|SERVICE_VERSION|服务版本号。建议按照va.b.c格式定义。|

3. 访问服务

```shell
curl http://localhost:8087/hello-world
```
