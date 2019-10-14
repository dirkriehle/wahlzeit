FROM adoptopenjdk/openjdk8-openj9:alpine-slim as builder

# Copy Gradle file
COPY *.gradle /app/

# Copy sources
COPY src /app/src

# Copy Gradle resources
COPY gradle /app/gradle
COPY gradlew /app/gradlew

WORKDIR /app

# Test project
RUN ./gradlew test

# Expose port
EXPOSE 8080

# Build & Run project (downloads Appengine SDK + builds + starts Dev Server)
CMD ./gradlew appengineRun