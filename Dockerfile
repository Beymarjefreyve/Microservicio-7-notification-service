# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render expects the app to listen on the port defined by the PORT environment variable
# We can pass it to Spring Boot via SERVER_PORT
ENV SERVER_PORT=8007

EXPOSE 8007

ENTRYPOINT ["java", "-jar", "app.jar"]
