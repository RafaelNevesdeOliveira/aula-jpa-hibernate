# Aula 11 — JPA, Hibernate e MongoDB

Laboratório da FIAP / Arquitetura Caixa. O `App` lê o JSON, grava no banco escolhido e abre o catálogo de exercícios.

Três formas, **sem Docker**:

| Forma | `aula.banco` | Tipo | Inspecionar em |
| --- | --- | --- | --- |
| 1. H2 | `h2` | SQL em arquivo | http://localhost:8082 |
| 2. PostgreSQL | `postgres` | SQL na máquina | pgAdmin ou `psql` |
| 3. MongoDB | `mongo` | NoSQL na máquina | Compass ou `mongosh` |

A chave fica em [`data/postgres/aula.properties`](data/postgres/aula.properties). **Não existe opção no menu do `App` para trocar o banco.** Você escolhe **antes** de rodar.

---

## Como escolher o banco

Valores aceitos: `h2`, `postgres`, `mongo`.

A ordem de leitura (o primeiro que existir ganha):

1. Comando / VM do IntelliJ: `-Daula.banco=...`
2. Variável de ambiente: `AULA_BANCO`
3. Arquivo [`data/postgres/aula.properties`](data/postgres/aula.properties): `aula.banco=...`
4. Se nada for informado: **H2**

### Opção A — arquivo (mais simples na aula)

Abra `data/postgres/aula.properties` e deixe **uma** linha ativa:

```properties
aula.banco=h2
# aula.banco=postgres
# aula.banco=mongo
```

| Quero | O que escrever |
| --- | --- |
| H2 | `aula.banco=h2` |
| PostgreSQL | `aula.banco=postgres` |
| MongoDB | `aula.banco=mongo` |

Depois, sempre o mesmo comando:

```sh
mvn -q compile exec:java
```

### Opção B — comando Maven (sem editar o arquivo)

Na pasta do `pom.xml`:

```sh
# H2
mvn -q compile exec:java -Daula.banco=h2

# PostgreSQL
mvn -q compile exec:java -Daula.banco=postgres

# MongoDB
mvn -q compile exec:java -Daula.banco=mongo
```

No Windows (PowerShell):

```powershell
mvn -q compile exec:java "-Daula.banco=h2"
mvn -q compile exec:java "-Daula.banco=postgres"
mvn -q compile exec:java "-Daula.banco=mongo"
```

O `-D` vale só nesta execução. O arquivo pode continuar com `h2`.

### Opção C — IntelliJ

1. Run → Edit Configurations → `App`
2. **VM options:** `-Daula.banco=mongo` (ou `h2` / `postgres`)
3. Working directory = pasta do `pom.xml`
4. Run

Ou altere `aula.banco` no arquivo e rode o `App` sem VM options.

### Opção D — variável de ambiente

```sh
export AULA_BANCO=mongo
mvn -q compile exec:java
```

```powershell
$env:AULA_BANCO = "mongo"
mvn -q compile exec:java
```

---

## Stack

| Peça | Versão | Papel |
| --- | --- | --- |
| Java | 25 | linguagem |
| Maven | 3.9+ | build e execução |
| JPA | 3.1 | API relacional |
| Hibernate | 6.6 | provedor JPA (H2 e Postgres) |
| H2 | 2.3 | SQL em arquivo |
| PostgreSQL | 16/17 | SQL local |
| MongoDB | 8.x | NoSQL local |
| Jackson | 2.18 | leitura dos JSONs |

---

## Antes de qualquer forma

1. Instale **JDK 25** e **Maven 3.9+**
   - Java: https://adoptium.net/temurin/releases/?version=25
   - Maven: https://maven.apache.org/download.cgi
2. Terminal **na pasta deste `pom.xml`**
3. Confira:

```sh
java -version
mvn -version
```

4. Escolha o banco (arquivo, `-Daula.banco=...` ou `AULA_BANCO`) — veja [Como escolher o banco](#como-escolher-o-banco)
5. Rode o `App`:

```sh
mvn -q compile exec:java
# ou, por exemplo:
mvn -q compile exec:java -Daula.banco=mongo
```

**IntelliJ:** File → Open na pasta do `pom.xml` → `org.caixaverso.App` → Run. Working directory = pasta do `pom.xml`.

O JSON em [`data/json`](data/json) é a fonte. Cada execução reaplica `contas.json` e `pessoas.json`.

---

## Forma 1 — H2 (padrão, sem instalar banco)

O H2 **não se instala no sistema**. O Maven baixa o JAR.

Site: https://www.h2database.com

### O que fazer

1. Em `data/postgres/aula.properties`:

```properties
aula.banco=h2
```

2. Rode:

```sh
mvn -q compile exec:java
```

3. Abra http://localhost:8082
4. Apague `jdbc:h2:~/test` e cole a JDBC URL abaixo
5. **Connect** → cole o SQL → **Run**

### Comandos

```sh
mvn -q compile exec:java
```

Console avulso (opcional):

```sh
mvn -q compile exec:java -Dexec.mainClass=org.caixaverso.infra.h2.ConsoleH2
```

```sql
select id, titular, saldo from conta order by id;
select id, nome, documento from pessoa order by id;
```

### Login

| Campo | Valor |
| --- | --- |
| Endereço | http://localhost:8082 |
| Driver | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:file:./data/h2/laboratorio;AUTO_SERVER=TRUE` |
| Usuário | `sa` |
| Senha | vazia |
| Arquivo | `data/h2/laboratorio.mv.db` |

### Zerar

```sh
rm data/h2/laboratorio.mv.db data/h2/laboratorio.lock.db data/h2/laboratorio.trace.db
```

```powershell
Remove-Item data\h2\laboratorio*.db -ErrorAction SilentlyContinue
```

---

## Forma 2 — PostgreSQL (SQL local, sem Docker)

Não use container e não use base corporativa da Caixa.

Links:

- Downloads: https://www.postgresql.org/download/
- Mac: https://www.postgresql.org/download/macosx/
- Windows: https://www.postgresql.org/download/windows/
- Instalador EDB: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
- `psql`: https://www.postgresql.org/docs/current/app-psql.html
- pgAdmin: https://www.pgadmin.org/docs/

### Passo comum

1. Instale o servidor e deixe a porta `5432` no ar
2. Crie o banco da aula:

```sh
# Mac
psql -d postgres -f data/postgres/criar-banco.sql

# Windows
psql -U postgres -f data/postgres/criar-banco.sql
```

3. Em `data/postgres/aula.properties`:

```properties
aula.banco=postgres
postgres.host=localhost
postgres.port=5432
postgres.database=aula11
postgres.user=aula
postgres.password=aula
```

4. Rode:

```sh
mvn -q compile exec:java
```

5. Confira:

```sh
psql -h localhost -U aula -d aula11 -c "select * from conta order by id;"
```

Senha: `aula`

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `5432` |
| Banco | `aula11` |
| Usuário | `aula` |
| Senha | `aula` |
| JDBC | `jdbc:postgresql://localhost:5432/aula11` |

### 2.1 Mac (Homebrew)

Homebrew: https://brew.sh  
Fórmula: https://formulae.brew.sh/formula/postgresql@17

```sh
docker ps --filter publish=5432
docker stop NOME_OU_ID   # se a 5432 estiver com Docker

brew install postgresql@17
echo 'export PATH="/opt/homebrew/opt/postgresql@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
brew services start postgresql@17
pg_isready
psql -d postgres -f data/postgres/criar-banco.sql
# aula.banco=postgres
mvn -q compile exec:java
```

```sh
brew services stop postgresql@17
brew services start postgresql@17
```

### 2.2 Windows / Caixa

1. MSI EDB: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads (16 ou 17)
2. Ou pacote do catálogo interno
3. Instale servidor + pgAdmin, porta `5432`, anote a senha do `postgres`
4. `services.msc` → postgresql → Running

```powershell
psql -U postgres -f data\postgres\criar-banco.sql
# se nao estiver no PATH:
& "C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -f data\postgres\criar-banco.sql
```

5. `aula.banco=postgres` e `mvn -q compile exec:java`
6. pgAdmin → `aula11` → Query Tool → `select * from conta;`

---

## Forma 3 — MongoDB (NoSQL, sem Docker)

JPA/Hibernate **não** acessa o Mongo. O `App` usa o driver oficial. O JSON vira documentos nas coleções `conta` e `pessoa`.

Links:

- Community Server: https://www.mongodb.com/try/download/community
- Compass: https://www.mongodb.com/try/download/compass
- `mongosh`: https://www.mongodb.com/docs/mongodb-shell/
- Mac: https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-os-x/
- Windows: https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-windows/

### Passo comum

1. Instale o MongoDB Community, porta `27017`
2. Em `data/postgres/aula.properties`:

```properties
aula.banco=mongo
mongo.host=localhost
mongo.port=27017
mongo.database=aula11
```

3. Rode:

```sh
mvn -q compile exec:java
```

4. Confira no Compass (`mongodb://localhost:27017`) ou:

```sh
mongosh mongodb://localhost:27017/aula11
db.conta.find()
db.pessoa.find()
```

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `27017` |
| Database | `aula11` |
| Coleções | `conta`, `pessoa` |
| URI | `mongodb://localhost:27017` |

### 3.1 Mac (Homebrew)

```sh
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community

docker ps --filter publish=27017
docker stop NOME_OU_ID   # se a 27017 estiver com Docker

mongosh --eval "db.runCommand({ ping: 1 })"
# aula.banco=mongo
mvn -q compile exec:java
```

```sh
brew services stop mongodb-community
brew services start mongodb-community
```

### 3.2 Windows / laboratório da Caixa

Não use Docker. Instale o servidor Windows e o Compass.

**Links**

- Community (MSI): https://www.mongodb.com/try/download/community  
  Platform: **Windows**, Package: **msi**, Version: 8.x
- Compass (se o MSI não trouxer): https://www.mongodb.com/try/download/compass
- Instalação oficial: https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-windows/
- Se a internet estiver bloqueada na Caixa: use o pacote do **catálogo interno**

**O que marcar no instalador**

1. Abra o MSI
2. Choose Setup Type: **Complete**
3. Service Configuration:
   - **Install MongoD as a Service** = marcado
   - Service Name: `MongoDB`
   - Run service as Network Service user
   - Port: `27017`
4. **Install MongoDB Compass** = marcado
5. Finish e deixe o serviço iniciar

**Conferir o serviço**

1. `Win + R` → `services.msc` → Enter
2. Procure **MongoDB Server** (ou `MongoDB`)
3. Status tem que ser **Running**. Se estiver Stopped: botão direito → Start

No PowerShell (como administrador, se o Start falhar):

```powershell
Get-Service MongoDB
Start-Service MongoDB
```

**PATH do mongosh** (se o comando não for reconhecido):

```powershell
& "C:\Program Files\MongoDB\Server\8.0\bin\mongosh.exe" --eval "db.runCommand({ ping: 1 })"
```

Em versões 7.x o caminho muda para `Server\7.0\bin`.

**Ligar a aula**

1. Abra `data\postgres\aula.properties` e deixe:

```properties
aula.banco=mongo
mongo.host=localhost
mongo.port=27017
mongo.database=aula11
```

2. PowerShell **na pasta do `pom.xml`**:

```powershell
mvn -q compile exec:java
```

3. IntelliJ: rode `org.caixaverso.App` (working directory = pasta do `pom.xml`)

**Ver os dados no Windows**

Compass:

1. Abra **MongoDB Compass**
2. URI: `mongodb://localhost:27017`
3. Connect
4. Database `aula11` → coleções `conta` e `pessoa`

`mongosh`:

```powershell
mongosh mongodb://localhost:27017/aula11
```

```javascript
db.conta.find()
db.pessoa.find()
```

Se `mongosh` não estiver no PATH:

```powershell
& "C:\Program Files\MongoDB\Server\8.0\bin\mongosh.exe" mongodb://localhost:27017/aula11
```

**Parar / iniciar depois**

```powershell
Stop-Service MongoDB
Start-Service MongoDB
```

**Se a porta 27017 já estiver ocupada**

```powershell
Get-NetTCPConnection -LocalPort 27017
```

Pare o processo que estiver usando (muitas vezes um container ou outro Mongo). A aula usa só o serviço Windows.

---

## Menu do App

O catálogo muda conforme o banco.

| Código | Classe | Aparece em |
| --- | --- | --- |
| `1` | `Exercicio01ObjetoMemoria` | H2, Postgres e Mongo |
| `2` | `Exercicio02ListarContasJpa` | H2 e Postgres |
| `3` | `Exercicio03ListarContasMongo` | Mongo |

---

## Arquitetura MVC

```text
org.caixaverso
├── App.java
├── model/          Conta, Pessoa
├── view/
├── controller/
├── repository/     JPA
├── infra/h2
├── infra/jpa
├── infra/mongo
├── infra/json
└── exercicios/
```

---

## De onde vêm os dados

| Arquivo | Destino relacional | Destino Mongo |
| --- | --- | --- |
| [`data/json/contas.json`](data/json/contas.json) | tabela `conta` | coleção `conta` |
| [`data/json/pessoas.json`](data/json/pessoas.json) | tabela `pessoa` | coleção `pessoa` |

---

## Como adicionar um exercício

1. Classe em `src/main/java/org/caixaverso/exercicios/`
2. Implemente `Exercicio`
3. Registre em `CatalogoExercicios.todos()`

```java
public class Exercicio04Novo implements Exercicio {
    public String codigo() { return "04"; }
    public String titulo() { return "Novo cenario"; }

    public void executar(ContextoAula contexto) {
        // relacional: contexto.jpa()
        // mongo: contexto.mongo()
    }
}
```

---

## Problemas comuns

| Sintoma | O que fazer |
| --- | --- |
| `Table "CONTA" not found` no H2 | Não use `jdbc:h2:~/test` |
| Arquivo H2 não aparece | Working directory = pasta do `pom.xml` |
| Porta 8082 ocupada | Pare o `App` anterior |
| `Connection refused` no Postgres | Serviço parado ou porta ≠ 5432 |
| `database "aula11" does not exist` | Rode `data/postgres/criar-banco.sql` |
| `Connection refused` no Mongo | Serviço parado ou porta ≠ 27017 |
| Compass vazio | Rode o `App` com `aula.banco=mongo` |

---

## Atalhos

```sh
mvn -q compile exec:java
mvn -q compile exec:java -Daula.banco=h2
mvn -q compile exec:java -Daula.banco=postgres
mvn -q compile exec:java -Daula.banco=mongo
```
