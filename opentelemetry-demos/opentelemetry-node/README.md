# Opentelemetry Node

## Quick start
```shell
$ make build_docker_image
$ docker run -e ACCESS_KEY_ID=<YOUR_ACCESS_KEY_ID> -e ACCESS_SECRET=<YOUR_ACCESS_SECRET> -e PROJECT=<YOUR_PROJECT> -e LOGSTORE=<YOUR_LOGSTORE> -e ENDPOINT=<YOUR_ENDPOINT> sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-node
```