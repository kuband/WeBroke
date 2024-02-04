FROM node:18-alpine as build-stage
WORKDIR /app
RUN npm install -g @angular/cli@16
COPY package.json package-lock.json proxy.conf.json ./
RUN npm ci
COPY . .

# Stage 2: Serve the angular app
EXPOSE 4200
CMD ["ng", "serve", "--host", "0.0.0.0", "--proxy-config", "proxy.conf.json"]

