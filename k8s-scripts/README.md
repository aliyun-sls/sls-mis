# K8S Scripts

## Quick Star

1. 修改配置, 根据实际情况修改services-config.yaml文件中的参数.

``` java-properties
ACCESS_KEY_ID=<YOUR_ACCESS_KEY_ID>
ACCESS_SECRET=<YOUR_ACCESS_SECRET>
PROJECT=<YOUR_PROJECT>
LOGSTORE=<YOUR_LOGSTORE>
ENDPOINT=<YOUR_ENDPOINT>
```

2. 运行configMap
``` shell
kubectl apply -f ./services-config.yaml
```

3. 运行容器

``` shell
kubectl apply -f ./ot-zipkin-context-propagate.yaml
```
