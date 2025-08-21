# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven wrapper + pom first (better layer caching)
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN ./mvnw -q -DskipTests package

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the fat jar built by Spring Boot
COPY --from=build /app/target/*.jar app.jar

# Render sets PORT; your application.properties already uses ${PORT:8080}
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
