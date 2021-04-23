# Opentelemetry Python Demo

Opentelemetry Python demo 使用 Opentelemetry-python 手动埋点采集 Trace，并上传到 SLS

## 依赖组件

- python
- peotry

## Quick start

1. 构建应用

```shell
poetry install
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

poetry run start --service_name="${SERVER_NAME}" --service_version="${SERVER_VERSION}" ${ACCESS_KEY_ID} ${ACCESS_KEY_SECRET} ${PROJECT} ${LOGSTORE} ${ENDPOINT}

```

3. 访问服务

```shell
curl http://localhost:8088/hello-world
```
