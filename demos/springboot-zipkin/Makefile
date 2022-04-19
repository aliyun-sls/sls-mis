.PHONY: build_project build_image build_latest_images push_image push_latest_image push_images
IMAGE_NAME=sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/springboot-zipkin
IMAGE_VERSION=1.0.0

build_project:
	./mvnw clean package
build_image: build_project
	docker build -t $(IMAGE_NAME):$(IMAGE_VERSION) .
build_latest_images: build_project
	docker build -t $(IMAGE_NAME):latest .
push_image: build_image
	docker push $(IMAGE_NAME):$(IMAGE_VERSION)
push_latest_image: build_latest_images
	docker push $(IMAGE_NAME):latest
push_images: push_image push_latest_image
