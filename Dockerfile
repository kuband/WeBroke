ARG BUILD_HOME=/app

FROM --platform=linux/x86_64 gradle:latest as build-image
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

#COPY gradle $APP_HOME/gradle
#COPY --chown=gradle:gradle . /home/gradle/src
#USER root
#RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

FROM --platform=linux/x86_64 amazoncorretto:17
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
ENV ARTIFACT_NAME=backend-0.0.1-SNAPSHOT.jar
COPY --from=build-image $APP_HOME/build/libs/$ARTIFACT_NAME .
#HEALTHCHECK --interval=30s --timeout=10s CMD curl --fail --silent localhost:8080/actuator/health || curl --fail --silent localhost:8081/actuator/health || exit 1
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}