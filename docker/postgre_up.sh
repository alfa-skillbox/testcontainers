#!/usr/bin/env bash
set -e
echo "Starting postgre container"
docker-compose up --force-recreate --remove-orphans -d postgre
docker-compose up --force-recreate --remove-orphans -d adminer