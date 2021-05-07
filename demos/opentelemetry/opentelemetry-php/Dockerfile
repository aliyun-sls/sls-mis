FROM composer:1.9.0
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ZIPKIN_ENDPOINT="" \
    SERVICE_NAME="opentelementry-php-0.0.1" \
    SERVICE_VERSION="0.0.1"

WORKDIR /opentelemetry-php

COPY composer.json ./
COPY . .
RUN composer install
EXPOSE 8087

CMD ["php", "-S", "0.0.0.0:8087", "src/server.php"]
