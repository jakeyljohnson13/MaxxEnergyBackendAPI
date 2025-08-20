# Stage 1: Build the app
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/MaxxEnergyBackendAPI-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
