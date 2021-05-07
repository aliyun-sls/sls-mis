JAVA = opentelemetry-java
DOTNET = opentelemetry-dotnet
GO = opentelemetry-go
NODE = opentelemetry-node
PHP = opentelemetry-php
PYTHON = opentelemetry-python

OPENTELEMETRYS = $(JAVA) $(DOTNET) $(GO) $(NODE) $(PHP) $(PYTHON)

build_images:
	for i in $(OPENTELEMETRYS) ; do ( cd $$i ; make build_image) ; done

push_images: build_images
	for i in $(OPENTELEMETRYS) ; do ( cd $$i; make push_images) ; done
