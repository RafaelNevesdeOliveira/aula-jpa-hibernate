# Como instalar a base H2

O H2 **não é um programa que se instala no sistema** (não há `.dmg`, instalador Windows nem serviço para ligar). Ele entra no projeto como dependência Maven. Na primeira compilação o Maven baixa o motor; na primeira execução do exercício local o próprio H2 **cria o arquivo** `data/h2/laboratorio.mv.db`.

## 1. Pré-requisitos

- **JDK 17 ou 25** (`java -version`)
- **Maven 3.9+** (`mvn -version`)
- Internet na primeira execução, para o Maven Central entregar o artefato `com.h2database:h2:2.3.232`

No IntelliJ: **File → Project Structure → Project SDK = Java 25** (ou 17).

## 2. Baixar o motor H2 (única “instalação”)

Na pasta deste projeto (`aula-JPA-Hibernate`):

```sh
mvn compile
```

Isso resolve a dependência já declarada no `pom.xml`:

```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <version>2.3.232</version>
</dependency>
```

Não é necessário baixar o ZIP do site do H2 para os exercícios. O JAR fica no repositório local do Maven (`~/.m2/repository/com/h2database/h2/`).

## 3. Criar a base local (arquivo no disco)

A base persistente **não vem pronta**. Ela nasce quando você roda o exercício que abre o arquivo:

```sh
mvn compile exec:java -Dexec.mainClass=exercicios.intermediario.Ex13BaseLocalJpaJdbc
```

Ou, no IntelliJ: abra `Ex13BaseLocalJpaJdbc` e execute o `main`.

O H2 cria automaticamente:

```
data/h2/laboratorio.mv.db
```

A tabela `conta` é criada pelo Hibernate (`hbm2ddl.auto = update`) na unidade JPA `local`.

## 4. Dados de conexão

Use estes valores no código, no console web e em qualquer cliente JDBC.

| Campo | Valor |
| --- | --- |
| Driver | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:file:./data/h2/laboratorio;AUTO_SERVER=TRUE` |
| Usuário | `sa` |
| Senha | *(vazia — deixe o campo em branco)* |
| Arquivo | `data/h2/laboratorio.mv.db` |
| Tabela | `conta` |

A URL absoluta também vale (é a que o `BancoLocal` imprime no console), por exemplo:

```text
jdbc:h2:file:/caminho/completo/aula-JPA-Hibernate/data/h2/laboratorio;AUTO_SERVER=TRUE
```

JPA (`Jpa.abrirLocal()`) e JDBC (`BancoLocal.abrirConexao()`) usam **o mesmo arquivo**.

## 5. Abrir o console web do H2

Com o motor já baixado pelo Maven:

```sh
mvn compile exec:java -Dexec.mainClass=apoio.AbrirConsoleH2
```

No IntelliJ: execute o `main` de `apoio.AbrirConsoleH2` e **deixe a execução rodando**.

1. Abra [http://localhost:8082](http://localhost:8082)
2. Cole a **JDBC URL** impressa no terminal
3. Usuário `sa`, senha vazia
4. **Connect**
5. No SQL, confira as linhas:

```sql
select id, titular, saldo from conta order by id;
```

## 6. Dois H2 no mesmo projeto

| Unidade | Onde | Instalação extra | Quem usa |
| --- | --- | --- | --- |
| `laboratorio` | memória | nenhuma — some ao fechar | exercícios isolados (`Jpa.abrir()`) |
| `local` | `data/h2/laboratorio.mv.db` | só o `mvn compile` + rodar o Ex13 | Ex13 e o console |

Não precisa instalar um segundo servidor. São dois modos do **mesmo JAR**.

## 7. Zerar ou reinstalar a base

Com o programa **parado** (console H2 fechado, nenhum `main` rodando):

1. Apague `data/h2/laboratorio.mv.db`
2. Apague também `laboratorio.lock.db` e `laboratorio.trace.db`, se existirem
3. Rode de novo o Ex13 — o arquivo e a tabela `conta` são recriados

A pasta `data/h2` pode permanecer; só o arquivo `.mv.db` é a base.

## 8. Problemas comuns

| Sintoma | O que fazer |
| --- | --- |
| `Could not resolve artifact com.h2database:h2` | Internet/proxy; rode `mvn compile` de novo |
| `Database may be already in use` | Feche o console H2 e outros `main`; depois tente de novo |
| Arquivo não aparece em `data/h2` | Confirme que o working directory é a pasta do `pom.xml` (não um subpacote) |
| Console abre, mas a tabela está vazia | Rode o Ex13 antes; a unidade em memória não grava neste arquivo |
| IntelliJ não acha `org.h2` | Maven reload / **Reimport**; o H2 precisa estar no classpath do módulo |

## 9. Opcional: H2 isolado (fora do Maven)

Só se quiser o instalador oficial, além do laboratório:

1. Baixe o ZIP em [https://www.h2database.com](https://www.h2database.com)
2. Extraia e execute o script `h2.sh` (macOS/Linux) ou `h2.bat` (Windows)
3. Na tela de login use a **mesma JDBC URL** da seção 4, apontando para o `laboratorio` desta pasta

Para a aula, o caminho Maven + `AbrirConsoleH2` é suficiente.
