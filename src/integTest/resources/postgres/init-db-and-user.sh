#!/bin/bash
set -ex

psql -d "$testS_DB" --username "$testS_USER" <<-EOSQL
CREATE DATABASE integ-alfa_skillbox_module_tests_db WITH OWNER test ENCODING = 'UTF8' CONNECTION LIMIT = -1;
GRANT ALL ON DATABASE integ-alfa_skillbox_module_tests_db TO test;
\c integ-alfa_skillbox_module_tests_db test;
\i /var/lib/testsql/init/init-db-schema-integTest.sql;
\dt ;
EOSQL