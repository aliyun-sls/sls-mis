IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mall/carts
IMAGE_VERSION := 1.1.0

build_project:
	 ./mvnw clean package -Dmaven.test.skip=true

build_images: build_project
	./mvnw docker:build

push_image: build_images
	docker push $(IMAGE_NAME):$(IMAGE_VERSION)

push_latest_image: build_images
	docker push $(IMAGE_NAME):latest

push_images: push_image push_latest_image