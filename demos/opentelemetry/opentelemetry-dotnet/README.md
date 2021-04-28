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
export PROJECT=<PROJECT_NAME>
export LOGSTORE=<LOGSTORE_NAME>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export SERVICE_NAME=opentelemetry-dotnet
export SERVICE_VERSION=1.0.0
export SERVICE_HOST=127.0.0.1

dotnet run --project WebApplication
```

3. 访问服务

```shell
curl http://localhost:8083/hello-world
```
