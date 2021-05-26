FROM node:12.19.1-alpine
ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    INSTANCE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-node-0.18.2" \
    SERVICE_VERSION="v0.18.2" \
    SERVICE_HOST="127.0.0.1"

WORKDIR /opentelemetry-node
COPY init-trace.js server.js utils.js package.json ./
RUN npm install

CMD ["npm", "start"]
