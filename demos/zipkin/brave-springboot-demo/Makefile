BRAVE_SPRINGBOOT_DEMO_IMAGE := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/brave-springboot-demo
BRAVE_SPRINGBOOT_DEMO_VERSION := 5.13.3

build_project:
	./mvnw clean package

build_image: build_project
	./mvnw docker:build

push_image: build_image
	docker push $(BRAVE_SPRINGBOOT_DEMO_IMAGE):$(BRAVE_SPRINGBOOT_DEMO_VERSION)

push_latest_image: build_image
	docker push $(BRAVE_SPRINGBOOT_DEMO_IMAGE):latest

push_images: push_image push_latest_image

