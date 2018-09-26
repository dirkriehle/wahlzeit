#!/usr/bin/env bash

TASK="appengineRun"

if [ ! -z "$1" ]; then
    TASK="$1"
fi

CURRENT_DIR=${PWD##*/}
if [ "$CURRENT_DIR" = "docker" ]; then
    cd ..
fi

docker run -it --rm --name wahlzeitJDK08 \
-v "$(pwd)":/usr/src/wahlzeit \
-w /usr/src/wahlzeit \
--network=host \
-p 8080:8080 \
gradle:4.10-jdk10-slim \
gradle "$TASK"