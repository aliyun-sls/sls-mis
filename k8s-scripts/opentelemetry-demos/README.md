# Opentelemetry-demos

## 修改配置文件
1. 根据实际配置修改`opentelemetry-configmap.yaml`

``` yaml
data:
  ACCESS_KEY_ID: <YOUR_ACCESS_KEY_ID>
  ACCESS_SECRET: <YOUR_ACCESS_SECRET>
  ENDPOINT: <YOUR_ENDPOINT>
  HTTPS_ENDPOINT: <YOUR_HTTPS_ENDPOINT>
  PROJECT: <YOUR_PROJECT>
  LOGSTORE: <YOUR_LOGSTORE>
```

## 部署ConfigMap
1. 创建namespace
``` shell
kubectl create namespace opentelemetry
```
2. 配置ConfigMap

``` shellsession
kubectl apply -f opentelemetry-configmap.yaml
```

## 部署服务
### 部署Opentelemetry-Java

1. 部署Opetelemetry Java服务
``` shell
kubectl apply -f opentelemetry-java.yaml

```
2. 访问服务

``` shell
curl <K8S_NODE>:30001/hello-world
```

### 部署Opentelemetry dotnet

1. 部署Opetelemetry dotnet服务
``` shell
kubectl apply -f opentelemetry-dotnet.yaml

```
2. 访问服务

``` shell
curl <K8S_NODE>:30002/hello-world
```


### 部署Opentelemetry go

1. 部署Opetelemetry go服务
``` shell
kubectl apply -f opentelemetry-go.yaml

```
2. 访问服务

``` shell
curl <K8S_NODE>:30003/hello-world
```


### 部署Opentelemetry node

1. 部署Opetelemetry node服务
``` shell
kubectl apply -f opentelemetry-node.yaml

```
2. 访问服务

``` shell
curl <K8S_NODE>:30004/hello-world
```


### 部署Opentelemetry php

1. 部署Opetelemetry node服务
``` shell
kubectl apply -f opentelemetry-php.yaml

```
2. 访问服务

``` shell
curl <K8S_NODE>:30006/hello-world
```


### 部署Opentelemetry python

1. 部署Opetelemetry python服务
``` shell
kubectl apply -f opentelemetry-python.yaml

```
2. 访问服务

``` shell
curl <K8S_NODE>:30007/hello-world
```

