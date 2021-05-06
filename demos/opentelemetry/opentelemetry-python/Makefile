PYTHON_IMAGE_NAME := sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/opentelemetry-python
PYTHON_IMAGE_VERSION := 1.1.0


build_project:
	poetry install

build_image: build_project
	docker build -t $(PYTHON_IMAGE_NAME):latest -t $(PYTHON_IMAGE_NAME):$(PYTHON_IMAGE_VERSION) .

push_image: build_image
	docker push $(PYTHON_IMAGE_NAME):$(PYTHON_IMAGE_VERSION)

push_latest_image:
	docker push $(PYTHON_IMAGE_NAME):latest


push_images: push_image push_latest_image

