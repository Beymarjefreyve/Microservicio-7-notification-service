# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render lo sobreescribirá con la variable PORT, pero mantenemos SERVER_PORT por consistencia
ENV SERVER_PORT=8007

EXPOSE 8007

ENTRYPOINT ["java", "-jar", "app.jar"]
