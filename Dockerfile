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
FROM jetty:9.4-jre11-slim


# Pull the built files from the builder container
COPY --from=builder /builder/build/libs/*.war /var/lib/jetty/webapps/wahlzeit.war

# Expose port
EXPOSE 8080
