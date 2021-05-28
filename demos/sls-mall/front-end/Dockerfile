FROM node:10-alpine
ENV NODE_ENV="production" \
    DOMAIN="" \
    OTLP_ENDPOINT=""

WORKDIR /sls-mall
COPY . .
RUN npm install
EXPOSE 8079

CMD ["npm", "start"]
