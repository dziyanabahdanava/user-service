FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /usr/src/apps
COPY build/libs/user-service-1.0-SNAPSHOT.jar user-service.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local", "/usr/src/apps/user-service.jar"]