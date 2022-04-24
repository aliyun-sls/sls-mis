FROM sls-registry.cn-beijing.cr.aliyuncs.com/sls-mall/front-end:1.5.0

RUN printf 'http://dl-cdn.alpinelinux.org/alpine/v3.14/main\nhttp://dl-cdn.alpinelinux.org/alpine/v3.14/community'>/etc/apk/repositories \
    && apk update \
    && apk upgrade apk-tools \
    && apk add stress-ng \
    && apk add iproute2-tc