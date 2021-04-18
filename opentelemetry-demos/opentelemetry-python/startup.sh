#!/bin/bash

function checkParam() {
  if [ "$1" = "" ]; then
    echo "Miss [$2] environment parameter"
    exit
  fi
}

checkParam "${ACCESS_KEY_ID}" "ACCESS_KEY_ID" && checkParam "$ACCESS_KEY_SECRET" "ACCESS_KEY_SECRET" && checkParam "$PROJECT" "PROJECT" &&
  checkParam "${LOGSTORE}" "LOGSTORE" && checkParam "${ENDPOINT}" "ENDPOINT"


poetry run start --service_name="${SERVER_NAME}" --service_version="${SERVER_VERSION}" ${ACCESS_KEY_ID} ${ACCESS_KEY_SECRET} ${PROJECT} ${LOGSTORE} ${ENDPOINT}
