FROM node:16.16.0-alpine

WORKDIR /app

COPY package*.json ./

ARG REACT_APP_SERVER_URL
ENV REACT_APP_SERVER_URL=$REACT_APP_SERVER_URL

RUN npm install

COPY . .

# VOLUME [ "./frontend","/app" ]

CMD ["npm","run","dev"]

# FROM node:16.16.0

# RUN mkdir -p /usr/src/app
# WORKDIR /usr/src/app

# COPY package*.json ./
# RUN npm install

# COPY . .

# EXPOSE 3000

# CMD ["npm","run","dev"]