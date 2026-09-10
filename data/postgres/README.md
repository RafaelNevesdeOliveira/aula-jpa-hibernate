# Duas formas de banco (sem Docker)

A chave está em [`aula.properties`](aula.properties).

```properties
aula.banco=h2
# aula.banco=postgres
```

Guia completo: [../../README.md](../../README.md)

## Forma 1 — H2

Não instala servidor. Rode o `App` e abra http://localhost:8082

```properties
aula.banco=h2
```

```sh
mvn -q compile exec:java
```

Login: usuário `sa`, senha vazia, JDBC `jdbc:h2:file:./data/h2/laboratorio;AUTO_SERVER=TRUE`

## Forma 2 — PostgreSQL

### Mac

Homebrew: https://brew.sh  
PostgreSQL 17: https://formulae.brew.sh/formula/postgresql@17

```sh
docker ps --filter publish=5432
docker stop NOME_OU_ID_DO_CONTAINER   # se a porta 5432 estiver com Docker

brew install postgresql@17
echo 'export PATH="/opt/homebrew/opt/postgresql@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
brew services start postgresql@17
pg_isready
psql -d postgres -f data/postgres/criar-banco.sql
```

### Windows / Caixa

Instalador: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads  
Download oficial: https://www.postgresql.org/download/windows/

```powershell
psql -U postgres -f data\postgres\criar-banco.sql
```

### Ligar a aula

```properties
aula.banco=postgres
```

```sh
mvn -q compile exec:java
```

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `5432` |
| Banco | `aula11` |
| Usuario | `aula` |
| Senha | `aula` |
| JDBC | `jdbc:postgresql://localhost:5432/aula11` |
