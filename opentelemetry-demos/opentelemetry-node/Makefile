NODE_DOCKER_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-node
NODE_DOCKER_VERSION := 0.18.0

clean_images:
	docker image prune -f

build_images: clean_images
	docker build -t $(NODE_DOCKER_NAME):$(NODE_DOCKER_VERSION) -t $(NODE_DOCKER_NAME):latest .

push_image: build_images
	docker push $(NODE_DOCKER_NAME):$(NODE_DOCKER_VERSION)

push_latest_image: build_images
	docker push $(NODE_DOCKER_NAME):latest

push_images: push_image push_latest_image
