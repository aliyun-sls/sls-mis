FROM node:12.19.1-alpine
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-node" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /opentelemetry-node
COPY init-trace.js server.js utils.js package.json ./
RUN npm install

CMD ["npm", "start"]
