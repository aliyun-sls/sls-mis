FROM composer:1.9.0
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-php" \
    SERVICE_VERSION="1.0.0"

WORKDIR /opentelemetry-php

COPY composer.json ./
COPY . .
RUN composer install
EXPOSE 8080

CMD ["php", "-S", "0.0.0.0:8080", "src/server.php"]