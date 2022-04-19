#!/bin/bash

java -DPROJECT=${PROJECT} -DINSTANCE=${INSTANCE} -DACCESS_KEY_ID=${ACCESS_KEY} -DACCESS_SECRET=${ACCESS_SECRITY} \
  -Dzipkin.baseUrl=${ZIPKIN_ENDPOINT} -D -jar springboot-zipkin.jar