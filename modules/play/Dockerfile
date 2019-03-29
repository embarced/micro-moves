FROM node:8

COPY package.json ./
RUN npm install

COPY index.html ./
COPY src ./src/

EXPOSE 7000
CMD [ "npm", "start" ]
