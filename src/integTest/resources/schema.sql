CREATE SCHEMA IF NOT EXISTS module_tests_schema AUTHORIZATION sa;

GRANT ALL ON SCHEMA module_tests_schema TO sa;

-- CREATE TABLE IF NOT EXISTS module_tests_schema.jsons (
--   id bigserial NOT NULL UNIQUE PRIMARY KEY,
--   json jsonb NOT NULL
-- );
--
-- ALTER TABLE module_tests_schema.jsons
--     OWNER to sa;