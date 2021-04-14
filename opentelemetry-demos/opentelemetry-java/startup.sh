#!/usr/bin/env bash
DEMO_HOME=$(
  cd "$(dirname "$0")"
  pwd
)

ACCESS_KEY_ID=$1
ACCESS_KEY_SECRET=$2
PROJECT=$3
LOGSTORE=$4
ENDPOINT=$5

cd ${DEMO_HOME} && ./mvnw clean package docker:build
docker run -itd -p 8080:8080 -e ACCESS_KEY_ID=${ACCESS_KEY_ID} -e ACCESS_KEY_SECRET=${ACCESS_KEY_SECRET} -e PROJECT=${PROJECT} -e LOGSTORE=${LOGSTORE} -e ENDPOINT=${ENDPOINT} alibaba-sls-demo/opentelemetry-java
echo "Server is up and you can visit http://localhost:8080/hello-world ."
