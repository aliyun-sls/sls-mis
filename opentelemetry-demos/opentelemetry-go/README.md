# Opentelemetry Go Demo

Opentelemetry Go Demo 采用 Opentelemetry Go 手动探针采集 Trace 数据，并发送给 SLS 服务

## Quick Start

```shell
export PROJECT=<PROJECT_NAME>
export LOGSTORE=<LOGSTORE_NAME>
export ACCESS_KEY_ID=<ACCESS_KEY_ID>
export ACCESS_SECRET=<ACCESS_SECRET>
export SERVICE_NAME=opentelemetry-go
export SERVICE_VERSION=1.0.0
export SERVICE_HOST=127.0.0.1

go build -o main src/server.go && ./main
```
