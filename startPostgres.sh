#!/usr/bin/env bash

docker build -t stash_postgres . && docker run -p 5432:5432 -d stash_postgres