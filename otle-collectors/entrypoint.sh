#!/bin/sh
#
set -x
sed -i -E "s/\{endpoint\}/${ENDPOINT}/g" /etc/otel/config.yaml
sed -i -E "s/\{project\}/${PROJECT}/g" /etc/otel/config.yaml
sed -i -E "s/\{access_key_id\}/${ACCESS_KEY_ID}/g" /etc/otel/config.yaml
sed -i -E "s/\{access_secret\}/${ACCESS_SECRET}/g" /etc/otel/config.yaml
sed -i -E "s/\{logstore\}/${LOGSTORE}/g" /etc/otel/config.yaml

exec "$@"
