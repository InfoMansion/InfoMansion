FROM openjdk:11-jre-slim

RUN mkdir -p /server

WORKDIR /server

# ARG  buildDir=build/unpack

# COPY ${buildDir}/BOOT-INF/classes/ app
# COPY ${buildDir}/BOOT-INF/lib/ lib

# COPY ${buildDir}/lib BOOT-INF/lib
# COPY ${buildDir}/app .


ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} /server/app.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","app.jar"]