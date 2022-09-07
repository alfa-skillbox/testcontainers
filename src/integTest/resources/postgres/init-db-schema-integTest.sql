CREATE SCHEMA IF NOT EXISTS module_tests_schema AUTHORIZATION test;

GRANT ALL ON SCHEMA module_tests_schema TO test;

ALTER DEFAULT PRIVILEGES IN SCHEMA module_tests_schema
GRANT ALL ON TABLES TO test WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA module_tests_schema
GRANT SELECT, USAGE ON SEQUENCES TO test WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA module_tests_schema
GRANT EXECUTE ON FUNCTIONS TO test WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA module_tests_schema
GRANT USAGE ON TYPES TO test WITH GRANT OPTION;


CREATE TABLE IF NOT EXISTS module_tests_schema.jsons (
  id bigserial NOT NULL UNIQUE PRIMARY KEY,
  json jsonb NOT NULL
);

ALTER TABLE module_tests_schema.jsons
    OWNER to test;

-- -- TODO just for info
 select nspname
 from pg_catalog.pg_namespace;

 SELECT * FROM information_schema.tables
 WHERE table_schema = 'module_tests_schema';