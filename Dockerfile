#########################################################
# First stage: image to build the application           #
#########################################################
FROM adoptopenjdk/openjdk11-openj9:alpine-slim as builder

WORKDIR /builder

# Copy Gradle file
COPY *.gradle /builder/

# Copy sources
COPY src /builder/src

# Copy Gradle resources
COPY gradle /builder/gradle
COPY gradlew /builder/gradlew

# Test project
RUN ./gradlew test

# Build project 
RUN ./gradlew assemble

#########################################################
# Second stage: image to run the application            #
#########################################################
FROM adoptopenjdk/openjdk11:alpine

# Pull the built files from the builder container
COPY --from=builder /builder/build/libs/*-all.jar /app.jar
RUN mkdir src

# Expose port
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
