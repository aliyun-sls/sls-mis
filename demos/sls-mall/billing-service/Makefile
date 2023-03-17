IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mall/billing
IMAGE_VERSION := 1.0.0

build_project:
	 mvn clean package -Dmaven.test.skip=true

build_images: build_project
	mvn docker:build

push_image: build_images
	docker push $(IMAGE_NAME):$(IMAGE_VERSION)

push_latest_image: build_images
	docker push $(IMAGE_NAME):latest

push_images: push_image push_latest_image