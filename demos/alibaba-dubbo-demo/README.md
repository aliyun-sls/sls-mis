# Alibaba Dubbo Demo

## Quick Start

1. 编译代码
```shell
./mvnw clean package
```

2. 根据 [文档](https://help.aliyun.com/document_detail/208914.html?spm=a2c4g.11186623.6.1016.60bad0b2tMYjEU) 配置SkyWalking采集环境

3. 启动Provider端
```shell
java -javaagent:/PATH/TO/skywalking-agent.jar -Dskywalking.agent.service_name=dubbo-provider-demo -jar dubbo-provider-demo.jar
```

4. 启动Consumer端
```shell
java -javaagent:/PATH/TO/skywalking-agent.jar -Dskywalking.agent.service_name=dubbo-consumer-demo -jar dubbo-consumer-demo.jar
```

5. 访问业务
```shell
curl http://localhost:8094/sayHello
```