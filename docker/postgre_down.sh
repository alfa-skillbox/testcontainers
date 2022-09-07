#!/usr/bin/env bash
set -ex
docker-compose rm -fsv postgre
docker-compose rm -fsv adminer