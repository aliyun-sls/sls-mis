FROM node:10-alpine
ENV NODE_ENV="production" \
    DOMAIN="" \
    OTLP_ENDPOINT=""
LABEL "app"="frontend"
LABEL "demo"="sls-mall"
LABEL "language"="nodejs"

WORKDIR /sls-mall
COPY . .
RUN npm install
EXPOSE 8079

CMD ["npm", "start"]
