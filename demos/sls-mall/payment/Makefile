NAME = sls-microservices/payment
INSTANCE = payment

.PHONY: default copy test docker

default: docker

copy:
	docker create --name $(INSTANCE) $(NAME)-dev
	docker cp $(INSTANCE):/app $(shell pwd)/app
	docker rm $(INSTANCE)

docker:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o app ./cmd/paymentsvc
	docker build -t $(NAME) -f ./docker/payment/Dockerfile-release .

test:
	GROUP=sls-microservices COMMIT=$(COMMIT) ./scripts/build.sh
	./test/test.sh unit.py
	./test/test.sh container.py --tag $(COMMIT)
