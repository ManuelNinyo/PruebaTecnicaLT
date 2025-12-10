FROM openjdk:17-jdk-slim

LABEL maintainer="manuel@prueba-tecnica.com"

WORKDIR /app

COPY target/PruebaTecnica-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
