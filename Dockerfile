# Stage 1: Build with JDK
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw

RUN ./mvnw clean install -DskipTests

# Stage 2: Run with JRE only
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port; Render overrides this anyway
EXPOSE 8080

# ✅ Force Spring Boot to use Render's $PORT
ENTRYPOINT ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]
