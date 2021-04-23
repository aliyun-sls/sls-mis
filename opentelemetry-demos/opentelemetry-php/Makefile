PHP_IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-php
PHP_IMAGE_VERSION := 0.0.1
clean_images:
	docker image prune -f

build_image: clean_images
	docker build -t $(PHP_IMAGE_NAME):$(PHP_IMAGE_VERSION) -t $(PHP_IMAGE_NAME):latest .

clean_container:
	docker container prune -f


push_image:
	docker push $(PHP_IMAGE_NAME):$(PHP_IMAGE_VERSION)


push_latest_image: 
	docker push $(PHP_IMAGE_NAME):latest


push_images: push_image push_latest_image
