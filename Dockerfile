FROM openjdk:17-jdk-slim

COPY build/libs/e-commerce-api.jar /app/app.jar
WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./app.jar"]
