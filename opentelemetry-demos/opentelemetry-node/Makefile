clean_images:
	docker image prune -f

build_docker_images: clean_images
	docker build -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-node:0.18.0 -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-node:latest .