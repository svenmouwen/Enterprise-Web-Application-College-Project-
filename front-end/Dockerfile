FROM node:alpine
RUN mkdir -p /front-end/dist
WORKDIR /front-end
COPY ./dist ./dist
ENTRYPOINT npx http-server -p ${PORT} ./dist
