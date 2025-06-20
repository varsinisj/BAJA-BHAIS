# Stage 1: Build the Spring Boot application
# Use a slim Java Development Kit (JDK) base image for building
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper, pom.xml, and src directory
# This ensures Maven can be used and all source code is available
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the Spring Boot application using Maven.
# The 'install' goal will build the JAR and place it in the target/ directory.
# '-DskipTests' is often used in Docker builds to speed up the process,
# but remove it if you want tests to run during the Docker build.
RUN ./mvnw clean install -DskipTests

# Stage 2: Create the final lightweight runtime image
# Use a Java Runtime Environment (JRE) base image for running the application
FROM eclipse-temurin:21-jre-jammy

# Set the working directory for the runtime environment
WORKDIR /app

# Copy the built JAR file from the build stage to the current stage
# The JAR file is typically located at target/<your-app-name>-<version>.jar
# We use a wildcard (*) to make it more flexible if the name changes slightly.
COPY --from=build /app/target/*.jar app.jar

# Expose the port that your Spring Boot application listens on.
# By default, Spring Boot uses port 8080.
EXPOSE 8080

# Command to run the Spring Boot application when the container starts.
# Spring Boot will automatically pick up the 'PORT' environment variable if present.
ENTRYPOINT ["java", "-jar", "app.jar"]

# For more robustness, you can add JVM memory settings
# ENTRYPOINT ["java", "-Xmx256m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
