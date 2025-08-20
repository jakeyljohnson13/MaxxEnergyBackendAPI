# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first (to cache dependencies)
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# Copy source
COPY src ./src

# Build the app
RUN mvn -q -DskipTests package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render will assign a port
ENV PORT=8080
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "/app/app.jar"]
