DOTNET_IMAGE_VERSION := 1.0.0-rc1
DOTNET_IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-dotnet
.PONY: clean clean_docker_image

clean_docker_image:
	docker image prune -f 
clean:
	rm -rf $(PWD)/dest

build_dependencies:
	dotnet restore

start: .PONY build_dependencies
	dotnet run --project WebApplication 

build_image: clean_docker_image
	docker build -t $(DOTNET_IMAGE_NAME):$(DOTNET_IMAGE_VERSION) -t $(DOTNET_IMAGE_NAME):latest .

push_image: build_image
	docker push $(DOTNET_IMAGE_NAME):$(DOTNET_IMAGE_VERSION)

push_latest_image: build_image
	docker push $(DOTNET_IMAGE_NAME):latest

push_images: push_image  push_latest_image
