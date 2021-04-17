build_project:
	export CGO_CFLAGS=-Wno-undef-prefix && env GOOS=linux GOARCH=arm go build -o main src/server.go

build_docker_image: build_project
	docker build -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-go:0.16.0 -t sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-go:latest .
