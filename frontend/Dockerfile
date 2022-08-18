FROM node:16.16.0-alpine

WORKDIR /app

COPY package*.json ./

ARG REACT_APP_SERVER_URL
ENV REACT_APP_SERVER_URL=$REACT_APP_SERVER_URL

RUN npm install

COPY . .

CMD ["npm","run","dev"]