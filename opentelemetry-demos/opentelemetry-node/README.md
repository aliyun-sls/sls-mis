# Opentelemetry Node demo

Opentelemetry Node Demo 采用 Opentelemetry-js 自动探针采集 Trace 数据，并上报到 SLS 服务。

## Quick start

```shell
export PROJECT=<PROJECT_NAME>
export LOGSTORE=<LOGSTORE_NAME>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export SERVICE_NAME=opentelemetry-node
export SERVICE_VERSION=1.0.0

npm install
npm run start
```
