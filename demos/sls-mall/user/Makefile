IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mall/user
IMAGE_VERSION := 1.4.0

build_project:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o app .

build_images: build_project
	docker build -t $(IMAGE_NAME):$(IMAGE_VERSION) -t $(IMAGE_NAME):latest .

push_image: build_images
	docker push $(IMAGE_NAME):$(IMAGE_VERSION)

push_latest_image: build_images
	docker push $(IMAGE_NAME):latest

push_images: push_image push_latest_image