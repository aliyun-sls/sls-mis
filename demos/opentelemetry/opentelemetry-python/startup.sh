#!/bin/bash

function checkParam() {
  if [ "$1" = "" ]; then
    echo "Miss [$2] environment parameter"
    exit
  fi
}

checkParam "${ACCESS_KEY_ID}" "ACCESS_KEY_ID" && checkParam "$ACCESS_SECRET" "ACCESS_SECRET" && checkParam "$PROJECT" "PROJECT" &&
  checkParam "${LOGSTORE}" "LOGSTORE" && checkParam "${HTTPS_ENDPOINT}" "HTTPS_ENDPOINT"


poetry run start --service_name=${SERVICE_NAME} --service_version=${SERVICE_VERSION} ${ACCESS_KEY_ID} ${ACCESS_SECRET} ${PROJECT} ${LOGSTORE} ${HTTPS_ENDPOINT}
