FROM maven:3.8.4-openjdk-11-slim as build

ARG SERVICE_NAME=technical_challenge_backend

COPY . /app

ADD settings.xml /root/.m2/settings.xml

WORKDIR /app/

RUN mvn clean install

FROM openjdk:11.0.5-jre-slim

ARG SERVICE_NAME=technical_challenge_backend
ARG JAR_FILE=target/${SERVICE_NAME}.jar
ARG SOURCE_FOLDER=/app

COPY --from=build /app/${JAR_FILE} /app/${SERVICE_NAME}.jar
COPY --from=build ${SOURCE_FOLDER}/src/main/resources/application*.properties /app/config/

ENTRYPOINT ["java","-jar","/app/technical_challenge_backend.jar"]
EXPOSE 8080
