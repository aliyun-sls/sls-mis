# Spring Boot Sleuth Demo(Zipkin)


## 快速启动
1. 编译工程

```shell
./mvnw clean package
```

2. 运行
```shell
export ACCESS_KEY_ID=<YOUR_ACCESS_KEY_ID>
export ACCESS_SECRET=<YOUR_ACCESS_SECRET>
export PROJECT=<YOUR_PROJECT_NAME>
export INSTANCE=<YOUR_TRACE_INSTANCE_NAME>
export ENDPOINT=<YOUR_ENDPOINT>
java -DACCESS_KEY_ID=${ACCESS_KEY_ID} -DACCESS_SECRET=${ACCESS_SECRET} -DPROJECT=${PROJECT} -DINSTANCE=${INSTANCE} -Dzipkin.baseUrl=${ENDPOINT} -jar target/springboot-zipkin.jar
```

各参数介绍:
|参数名|参数描述|
|:---|:---|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。|
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。|
|PROJECT_NAME|日志服务Project名称。 |
|INSTANCE|Trace服务实例名称。 |
|ENDPOINT|接入地址，格式为https://${project}.${region-endpoint}/zipkin，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。 |
