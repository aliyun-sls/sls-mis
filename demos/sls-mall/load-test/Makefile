IMAGE_NAME = sls-registry.cn-beijing.cr.aliyuncs.com/sls-mall/load-test
IMAGE_VERSION = 1.1.0

build_image:
	docker build -t $(IMAGE_NAME):$(IMAGE_VERSION) -t $(IMAGE_NAME):latest .

push_image: build_image
	docker push $(IMAGE_NAME):$(IMAGE_VERSION)

push_latest_image: build_image
	docker push $(IMAGE_NAME):latest

push_images: push_image push_latest_image
