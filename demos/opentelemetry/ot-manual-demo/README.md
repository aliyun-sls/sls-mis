# OpenTelemetry Manual API Demo

## Quick Start
```shell
## 构建工程
mvn clean package

## 运行工程
export ENDPOINT=${ENDPOINT}
export INSTANCE_ID=${YOUR_INSTANCE_ID}
export PROJECT=${YOUR_PROJECT}
export ACCESS_KEY_ID=${ACCESS_KEY_ID}
export ACCESS_SECRET=${ACCESS_SECRET}
java -jar target/ot-mavnual-demo.jar
```

各参数详细介绍:

|参数名|参数描述|
|:---|:---|
|ACCESS_KEY_ID| 阿里云账号AccessKey ID。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。|
|ACCESS_SECRET| 阿里云账号AccessKey Secret。<br/>建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。|
|PROJECT|日志服务Project名称。 |
|INSTANCE|Trace服务实例名称。 |
|ENDPOINT|接入地址，格式为https://${project}.${region-endpoint}:10010，其中：<br/> ${project}：日志服务Project名称。<br/>${region-endpoint}：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。 |
