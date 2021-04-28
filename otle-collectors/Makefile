JAEGER_OTEL_COLLECTOR_IMAGE_NAME:=sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/otel-collector-jaeger
ZIPKIN_OTEL_COLLECTOR_IMAGE_NAME:=sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/otel-collector-zipkin
OTEL_COLLECTOR_IMAGE_NAME:=sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/otel-collector
OPENCENSUS_OTEL_COLLECTOR_IMAGE_NAME:=sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/otel-collector-opensensus

IMAGE_VERSION:= 0.24.0

build_jaeger_image:
	docker build -t $(JAEGER_OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION) -t $(JAEGER_OTEL_COLLECTOR_IMAGE_NAME):latest --build-arg VENDOR=jaeger .

build_zipkin_image:
	docker build -t $(ZIPKIN_OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION) -t $(ZIPKIN_OTEL_COLLECTOR_IMAGE_NAME):latest --build-arg VENDOR=zipkin .

build_otel_image:
	docker build -t $(OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION) -t $(OTEL_COLLECTOR_IMAGE_NAME):latest --build-arg VENDOR=opentelemetry .

build_opencensus_image:
	docker build -t $(OPENCENSUS_OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION) -t $(OPENCENSUS_OTEL_COLLECTOR_IMAGE_NAME):latest --build-arg VENDOR=opencensus .

build_images: build_jaeger_image build_zipkin_image build_otel_image build_opencensus_image


push_jaeger_image: build_jaeger_image
	docker push $(JAEGER_OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION)
	docker push $(JAEGER_OTEL_COLLECTOR_IMAGE_NAME):latest

push_zipkin_image: build_zipkin_image
	docker push $(ZIPKIN_OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION)
	docker push $(ZIPKIN_OTEL_COLLECTOR_IMAGE_NAME):latest

push_otel_image: build_otel_image
	docker push $(OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION)
	docker push $(OTEL_COLLECTOR_IMAGE_NAME):latest

push_opencensus_image: build_opencensus_image
	docker push $(OPENCENSUS_OTEL_COLLECTOR_IMAGE_NAME):$(IMAGE_VERSION)
	docker push $(OPENCENSUS_OTEL_COLLECTOR_IMAGE_NAME):latest

push_images: push_jaeger_image push_zipkin_image push_otel_image push_opencensus_image
