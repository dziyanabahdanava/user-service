FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /usr/src/apps
COPY build/libs/*.jar user-service.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "--spring.config.location=/usr/src/apps/config/application-local.yml", "/usr/src/apps/user-service.jar"]