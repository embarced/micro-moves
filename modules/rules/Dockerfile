FROM node:8

WORKDIR /usr/src/app
COPY package*.json ./

RUN npm install

COPY . .
RUN npm test

EXPOSE 8081
CMD [ "npm", "start" ]