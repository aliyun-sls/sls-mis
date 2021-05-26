build_project:
	./mvnw clean package -Dmaven.test.skip=true

build_images: build_project
	./mvnw docker:build