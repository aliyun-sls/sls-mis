JAVA_IMAGE_IMAGE := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-java
JAVA_IMAGE_VERSION := 2.0.0

build_project:
	./mvnw clean package

build_image: build_project
	./mvnw docker:build

start: build_project
	java -javaagent:$(PWD)/target/opentelemetry-javaagent-all.jar -Dotel.exporter.otlp.protocol=grpc -Dotel.exporter.otlp.endpoint=$(ENDPOINT) -Dotel.exporter.otlp.compression=gzip -Dotel.exporter.otlp.headers=x-sls-otel-project=$(PROJECT),x-sls-otel-instance-id=$(LOGSTORE),x-sls-otel-ak-id=$(ACCESS_KEY_ID),x-sls-otel-ak-secret=$(ACCESS_SECRET)  -Dotel.resource.attributes=service.name=$(SERVICE_NAME),service.version=$(SERVICE_VERSION),host.name=$(SERVICE_HOST) -jar $(PWD)/target/opentelemetry-java.jar

push_image: build_image
	docker push $(JAVA_IMAGE_IMAGE):$(JAVA_IMAGE_VERSION)

push_latest_image: build_image
	docker push $(JAVA_IMAGE_IMAGE):latest 

push_images: push_image  push_latest_image
