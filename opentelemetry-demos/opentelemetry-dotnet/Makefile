clean_docker_image:
	docker image prune -f 
	
build_docker_image: clean_docker_image
	docker build -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-dotnet:1.0.0-rc1 -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-dotnet:latest .