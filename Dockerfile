#########################################################
# First stage: image to build the application           #
#########################################################
FROM adoptopenjdk/openjdk8-openj9:alpine-slim as builder

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

# Build project (downloads Appengine SDK + builds + explode WAR)
RUN ./gradlew appengineExplodeApp

#########################################################
# Second stage: image to run the application            #
#########################################################
FROM adoptopenjdk/openjdk8-openj9:alpine-slim

RUN mkdir /app
WORKDIR /app

# Pull the built files from the builder container
COPY --from=builder /builder/build /app/build

# Pull the Appengine SDK from the builder container
COPY --from=builder /root/.gradle/appengine-sdk/appengine-java-sdk-1.9.76/ /root/.gradle/appengine-sdk/appengine-java-sdk-1.9.76/

# Expose port
EXPOSE 8080

# Do the same as ./gradlew appengineRun for starting the dev server
ENTRYPOINT ["/opt/java/openjdk/bin/java", "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000", "-Dfile.encoding=UTF-8", "-classpath", "/root/.gradle/wrapper/dists/gradle-4.10-bin/bg6py687nqv2mbe6e1hdtk57h/gradle-4.10/lib/gradle-launcher-4.10.jar:/root/.gradle/appengine-sdk/appengine-java-sdk-1.9.76/lib/appengine-tools-api.jar", "com.google.appengine.tools.development.DevAppServerMain", "--property=kickstart.user.dir=/app", "--no_java_agent", "--port=8080", "--address=localhost", "--allow_remote_shutdown", "/app/build/exploded-app"]
