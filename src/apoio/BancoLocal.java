package apoio; // Acesso JDBC ao mesmo arquivo H2 usado pela unidade JPA "local".

import java.nio.file.Files; // Cria a pasta data/h2 se ainda não existir.
import java.nio.file.Path; // Caminho do arquivo .mv.db no disco.
import java.sql.Connection; // Conexão JDBC compartilhada com a JPA.
import java.sql.DriverManager; // Abre o H2 sem EntityManager.
import java.sql.SQLException; // Falha de rede/arquivo/SQL.

/**
 * Base H2 em arquivo local. JPA e JDBC apontam para o mesmo caminho.
 * Os exercícios isolados continuam na unidade em memória (Jpa.abrir).
 */
public final class BancoLocal {
    public static final String USUARIO = "sa"; // Usuário de laboratório; sem senha.
    public static final String SENHA = ""; // Vazio de propósito — não use em produção.

    private BancoLocal() { } // Só métodos estáticos.

    /** Pasta data/h2 relativa ao diretório de execução do projeto. */
    public static Path pasta() {
        return Path.of(System.getProperty("user.dir"), "data", "h2"); // Evita gravar fora do módulo.
    }

    /** Prefixo do arquivo; o H2 cria laboratorio.mv.db nesta pasta. */
    public static Path arquivo() {
        return pasta().resolve("laboratorio"); // Sem extensão: o motor acrescenta .mv.db.
    }

    /** URL JDBC do arquivo; AUTO_SERVER permite JPA e JDBC abertos ao mesmo tempo. */
    public static String url() {
        return "jdbc:h2:file:" + arquivo().toAbsolutePath() + ";AUTO_SERVER=TRUE";
    }

    /** Garante que data/h2 exista antes de abrir fábrica ou conexão. */
    public static void garantirPasta() {
        try {
            Files.createDirectories(pasta()); // Não falha se a pasta já existir.
        } catch (Exception erro) {
            throw new IllegalStateException("Não foi possível criar " + pasta(), erro);
        }
    }

    /** Abre conexão JDBC no mesmo arquivo da unidade JPA local. */
    public static Connection abrirConexao() throws SQLException {
        garantirPasta(); // A pasta precisa existir antes do primeiro INSERT.
        return DriverManager.getConnection(url(), USUARIO, SENHA); // Mesmas credenciais do XML.
    }

    /** Caminho do arquivo físico que o H2 grava no disco. */
    public static Path arquivoMv() {
        return Path.of(arquivo().toAbsolutePath() + ".mv.db"); // Extensão padrão do H2 2.x.
    }
}
