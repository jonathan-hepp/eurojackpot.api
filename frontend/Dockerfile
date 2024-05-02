FROM node:22-alpine

WORKDIR /frontend-app/

COPY public/ /frontend-app/public
COPY src/ /frontend-app/src
COPY package.json /frontend-app/

RUN npm install

CMD ["npm", "start"]