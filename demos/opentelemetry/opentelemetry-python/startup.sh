#!/bin/bash

function checkParam() {
  if [ "$1" = "" ]; then
    echo "Miss [$2] environment parameter"
    exit
  fi
}

checkParam "${ACCESS_KEY_ID}" "ACCESS_KEY_ID" && checkParam "$ACCESS_SECRET" "ACCESS_SECRET" && checkParam "$PROJECT" "PROJECT" &&
  checkParam "${INSTANCE}" "INSTANCE" && checkParam "${ENDPOINT}" "ENDPOINT"


poetry run start --service-name=${SERVICE_NAME} --service-version=${SERVICE_VERSION} ${ACCESS_KEY_ID} ${ACCESS_SECRET} ${PROJECT} ${INSTANCE} ${ENDPOINT}
