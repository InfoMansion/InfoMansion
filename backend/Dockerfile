FROM openjdk:11-jre-slim

RUN mkdir -p /server

WORKDIR /server

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} /server/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","app.jar"]