GO_IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-go
GO_IMAGE_VERSION := 0.16.0

build_project:
	export CGO_CFLAGS=-Wno-undef-prefix && env GOOS=linux GOARCH=arm go build -o main src/server.go

build_image: build_project
	docker build -t $(GO_IMAGE_NAME):$(GO_IMAGE_VERSION) -t $(GO_IMAGE_NAME):latest .

push_latest_image:	
	docker push $(GO_IMAGE_NAME):latest

push_image: build_image
	docker push $(GO_IMAGE_NAME):$(GO_IMAGE_VERSION)

push_images: push_image  push_latest_image

start: build_project
	./main	
