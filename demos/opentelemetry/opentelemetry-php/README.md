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
export PROJECT=<PROJECT_NAME>
export LOGSTORE=<LOGSTORE_NAME>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export SERVICE_NAME=opentelemetry-java
export SERVICE_VERSION=1.0.0
export SERVICE_HOST=127.0.0.1

php -S '0.0.0.0:8087' src/server.php
```

3. 访问服务

```shell
curl http://localhost:8087/hello-world
```
