package org.caixaverso.infra.h2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class BancoH2 {

    public static final String USUARIO = "sa";
    public static final String SENHA = "";
    public static final int PORTA_CONSOLE = 8082;

    private BancoH2() {
    }

    public static Path pasta() {
        return Path.of(System.getProperty("user.dir"), "data", "h2");
    }

    public static Path arquivo() {
        return pasta().resolve("laboratorio");
    }

    public static Path arquivoMv() {
        return Path.of(arquivo().toAbsolutePath() + ".mv.db");
    }

    public static String url() {
        return "jdbc:h2:file:" + arquivo().toAbsolutePath() + ";AUTO_SERVER=TRUE";
    }

    public static String urlRelativa() {
        return "jdbc:h2:file:./data/h2/laboratorio;AUTO_SERVER=TRUE";
    }

    public static void garantirPasta() {
        try {
            Files.createDirectories(pasta());
        } catch (Exception erro) {
            throw new IllegalStateException("Nao foi possivel criar " + pasta(), erro);
        }
    }

    public static Connection abrirConexao() throws SQLException {
        garantirPasta();
        return DriverManager.getConnection(url(), USUARIO, SENHA);
    }
}
