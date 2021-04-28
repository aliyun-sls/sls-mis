NAME = sls-microservices/catalogue
DBNAME = sls-microservices/catalogue-db

TAG=$(TRAVIS_COMMIT)

INSTANCE = catalogue

.PHONY: default copy test docker

default: docker

docker:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o app ./cmd/cataloguesvc
	docker build -t $(NAME) -f ./docker/catalogue/Dockerfile .

test: 
	GROUP=sls-microservices COMMIT=test ./scripts/build.sh
	./test/test.sh unit.py
	./test/test.sh container.py --tag $(TAG)

dockertravisbuild: build
	docker build -t $(NAME):$(TAG) -f docker/catalogue/Dockerfile-release docker/catalogue/
	docker build -t $(DBNAME):$(TAG) -f docker/catalogue-db/Dockerfile docker/catalogue-db/
	docker login -u $(DOCKER_USER) -p $(DOCKER_PASS)
	scripts/push.sh
