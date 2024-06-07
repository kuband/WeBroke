FROM node:21.1.0-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm config set legacy-peer-deps true
RUN npm install
COPY .. .
RUN npm run build

# Stage 2: Serve the angular app with Nginx
FROM nginx:alpine
COPY --from=build-stage /app/dist/frontend /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
#COPY ./ssl /etc/nginx/ssl

