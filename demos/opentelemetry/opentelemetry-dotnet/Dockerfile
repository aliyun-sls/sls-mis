FROM sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/dotnet/sdk:5.0-buster-slim
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-dotnet-1.1.0-beta2" \
    SERVICE_VERSION="v1.1.0-beta2" \
    SERVICE_HOST="127.0.0.1" \
    LD_LIBRARY_PATH=/usr/local/lib

WORKDIR /opentelemetry-dotnet

COPY *.sln .
COPY . .
RUN sed -i s@/deb.debian.org/@/mirrors.aliyun.com/@g /etc/apt/sources.list && apt-get clean \
    && apt-get update && apt update && apt install -y libc-dev && dotnet restore

CMD ["dotnet", "run", "--project", "WebApplication"]
