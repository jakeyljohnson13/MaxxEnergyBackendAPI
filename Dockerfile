# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven wrapper + pom first (so deps cache)
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Ensure wrapper is executable and prefetch deps
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN ./mvnw -q -DskipTests package

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the fat jar produced by Spring Boot
COPY --from=build /app/target/*.jar app.jar

# Render provides PORT; Spring uses server.port=${PORT:8080}
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
