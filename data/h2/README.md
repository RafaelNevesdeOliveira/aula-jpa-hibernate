# Base H2 local

Guia completo de instalação: **[../README_H2.md](../README_H2.md)**.

Resumo:

- O H2 não se instala no sistema. `mvn compile` baixa o JAR.
- `laboratorio.mv.db` nasce ao rodar `Ex13BaseLocalJpaJdbc`.
- JDBC URL: `jdbc:h2:file:./data/h2/laboratorio;AUTO_SERVER=TRUE`
- Usuário: `sa` — senha vazia
- Tabela: `conta` (entidade `main.java.org.caixaverso.exercicios.Conta`)

JPA (`Jpa.abrirLocal()`) e JDBC (`BancoLocal.abrirConexao()`) apontam para este arquivo.

Para zerar, apague `laboratorio.mv.db` (e `.lock.db` / `.trace.db`, se existirem) com o programa parado.
