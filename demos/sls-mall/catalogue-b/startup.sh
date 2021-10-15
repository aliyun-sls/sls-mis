#!/usr/bin/env sh

/sls-mall/app -DSN  "${MYSQL_USER}:${MYSQL_PASSWORD}@tcp(${MYSQL_HOST}:3306)/${MYSQL_DATABASE}"