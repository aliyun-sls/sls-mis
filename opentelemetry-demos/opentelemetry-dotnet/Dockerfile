FROM sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/dotnet/sdk:5.0-buster-slim AS build
WORKDIR /source

COPY *.sln .
COPY . .

WORKDIR /source/WebApplication
COPY sources.list /etc/apt/sources.list
RUN apt update && apt install -y libc-dev && \
    dotnet restore && dotnet publish -c release -o /opentelemetry-dotnet --no-restore 

FROM sls-registry.cn-beijing.cr.aliyuncs.com/sls-mis/dotnet/aspnet:5.0-buster-slim
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
COPY --from=build /opentelemetry-dotnet ./

CMD ["dotnet", "WebApplication.dll"]