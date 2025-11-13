# syntax=docker/dockerfile:1

### Build stage ###
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy wrapper and pom first for better caching
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Copy sources and build
COPY src ./src
RUN chmod +x ./mvnw && ./mvnw -B -DskipTests clean package

### Run stage ###
FROM eclipse-temurin:17-jre
WORKDIR /app

# Use ARG so jar wildcard works even if version changes
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/${JAR_FILE} ./app.jar

# Expose the port your app uses (change SERVER_PORT via env if needed)
EXPOSE 8082

ENTRYPOINT ["java","-jar","/app/app.jar"]

# Optional: small healthcheck (requires actuator / health endpoint)
# HEALTHCHECK --interval=30s --timeout=3s \
#   CMD curl -f http://localhost:8082/actuator/health || exit 1
