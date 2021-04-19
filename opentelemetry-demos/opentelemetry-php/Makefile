clean_images:
	docker image prune -f

build_docker_images: clean_images
	docker build -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-php:1.0.0 -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-php:latest .

clean_container:
	docker container prune -f