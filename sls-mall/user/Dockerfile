FROM golang:1.15-alpine
ENV sourcesdir /go/src/github.com/sls-microservices-demo/user/
ENV MONGO_HOST mytestdb:27017
ENV HATEAOS user
ENV USER_DATABASE mongodb

COPY . ${sourcesdir}
RUN go build

ENTRYPOINT user
EXPOSE 8084
