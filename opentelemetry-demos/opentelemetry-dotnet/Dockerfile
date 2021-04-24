FROM sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/dotnet/sdk:5.0-buster-slim
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-dotnet" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1" \
    LD_LIBRARY_PATH=/usr/local/lib

WORKDIR /opentelemetry-dotnet

COPY *.sln .
COPY . .
COPY sources.list /etc/apt/sources.list
RUN apt update && apt install -y libc-dev && dotnet restore

CMD ["dotnet", "run", "--project", "WebApplication"]
