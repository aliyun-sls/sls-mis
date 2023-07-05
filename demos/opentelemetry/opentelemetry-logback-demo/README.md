# OpenTelemetry logback demo

OpenTelemetry logback demo通过OpenTelemetry JavaAgent和OpenTelemetry Logback Appender上传日志到阿里云日志服务。

## Pre-requirements

1. OpenTelemetry Javaagent
2. OpenTelemetry Collector Contrib

## Quick Start

1. 编译package

```shell
mvn clean package
```

2. 启动OpenTelemetry Collector Contrib，以下是OpenTelemetry Collector Contrib的配置文件示例：

```yaml
receivers:
  otlp:
    protocols:
      grpc:
      http:

exporters:
  logging:
    verbosity: detailed
    sampling_initial: 5
    sampling_thereafter: 200
  alibabacloud_logservice/logs:
    endpoint: "cn-beijing.log.aliyuncs.com"
    project: "${YOUR_PROJECT}"
    logstore: "${YOUR_INSTANCE_ID}-logs"
    access_key_id: ""
    access_key_secret: ""
  alibabacloud_logservice/metrics:
    endpoint: "cn-beijing.log.aliyuncs.com"
    project: "${YOUR_PROJECT}"
    logstore: "${YOUR_INSTANCE_ID}-metrics"
    access_key_id: ""
    access_key_secret: ""
  alibabacloud_logservice/traces:
    endpoint: "cn-beijing.log.aliyuncs.com"
    project: "${YOUR_PROJECT}"
    logstore: "${YOUR_INSTANCE_ID}-traces"
    access_key_id: ""
    access_key_secret: ""

service:
  pipelines:
    traces:
      receivers: [ otlp ]
      exporters: [ logging, alibabacloud_logservice/traces ]
    metrics:
      receivers: [ otlp ]
      exporters: [ logging, alibabacloud_logservice/metrics ]
    logs:
      receivers: [ otlp ]
      exporters: [ logging, alibabacloud_logservice/logs ]
  telemetry:
    logs:
      level: "debug"
```

注：以上配置文件中的`${YOUR_PROJECT}`和`${YOUR_INSTANCE_ID}`需要替换为您的阿里云项目ID和日志服务实例ID。

3. 启动应用

```shell
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
export OTEL_LOGS_EXPORTER=otlp
export OTEL_RESOURCE_ATTRIBUTES=service.name=test,service.version=v0.1.0,host.name=test,testabc=5678

java -javaagent:/path/to/opentelemetry-agent.jar \
     -jar target/opentelemetry-log-demo-1.0-SNAPSHOT.jar
```

4. 查看日志服务