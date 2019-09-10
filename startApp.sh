#!/usr/bin/env bash

docker stop $(docker ps -a -q --filter ancestor=stash_postgres --format="{{.ID}}") && \
./startPostgres.sh && \
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/stash" && \
export SPRING_DATASOURCE_USERNAME="stash" && \
export SPRING_DATASOURCE_PASSWORD="stash" && \
mvn clean install && \
cd app && \
mvn spring-boot:run