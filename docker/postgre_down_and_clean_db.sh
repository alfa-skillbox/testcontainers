#!/usr/bin/env bash
set -ex
docker-compose rm -fsv postgre
docker-compose rm -fsv adminer
echo "Trying to remove all inside ./data/postgre folder"
rm -r ./data/postgre/*
echo "Removed!"