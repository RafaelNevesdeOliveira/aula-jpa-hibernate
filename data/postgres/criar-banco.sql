-- Rode no psql ou no pgAdmin conectado no banco postgres (usuario da instalacao local).
-- Laboratorio da aula. Nao aponte para base corporativa da Caixa.

CREATE USER aula WITH PASSWORD 'aula';
CREATE DATABASE aula11 OWNER aula;

\connect aula11
GRANT ALL ON SCHEMA public TO aula;
ALTER SCHEMA public OWNER TO aula;
