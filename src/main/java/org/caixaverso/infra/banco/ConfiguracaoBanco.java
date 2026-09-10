package org.caixaverso.infra.banco;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfiguracaoBanco {

    private final TipoBanco tipo;
    private final String host;
    private final String porta;
    private final String database;
    private final String usuario;
    private final String senha;

    private ConfiguracaoBanco(
            TipoBanco tipo,
            String host,
            String porta,
            String database,
            String usuario,
            String senha
    ) {
        this.tipo = tipo;
        this.host = host;
        this.porta = porta;
        this.database = database;
        this.usuario = usuario;
        this.senha = senha;
    }

    public static ConfiguracaoBanco carregar() {
        Properties props = new Properties();
        carregarArquivo(props, Path.of(System.getProperty("user.dir"), "data", "postgres", "aula.properties"));
        String tipoInformado = primeiroNaoVazio(
                System.getProperty("aula.banco"),
                System.getenv("AULA_BANCO"),
                props.getProperty("aula.banco"),
                "h2"
        );
        TipoBanco tipo = "postgres".equalsIgnoreCase(tipoInformado.strip()) ? TipoBanco.POSTGRES : TipoBanco.H2;
        return new ConfiguracaoBanco(
                tipo,
                props.getProperty("postgres.host", "localhost"),
                props.getProperty("postgres.port", "5432"),
                props.getProperty("postgres.database", "aula11"),
                props.getProperty("postgres.user", "aula"),
                props.getProperty("postgres.password", "aula")
        );
    }

    public boolean ehH2() {
        return tipo == TipoBanco.H2;
    }

    public boolean ehPostgres() {
        return tipo == TipoBanco.POSTGRES;
    }

    public TipoBanco tipo() {
        return tipo;
    }

    public String usuario() {
        return usuario;
    }

    public String senha() {
        return senha;
    }

    public String urlPostgres() {
        return "jdbc:postgresql://" + host + ":" + porta + "/" + database;
    }

    public String unidadeJpa() {
        return ehPostgres() ? "postgres" : "local";
    }

    private static String primeiroNaoVazio(String... valores) {
        for (String valor : valores) {
            if (valor != null && !valor.isBlank()) {
                return valor;
            }
        }
        return "h2";
    }

    private static void carregarArquivo(Properties props, Path arquivo) {
        if (!Files.exists(arquivo)) {
            return;
        }
        try (InputStream entrada = Files.newInputStream(arquivo)) {
            props.load(entrada);
        } catch (IOException erro) {
            throw new IllegalStateException("Nao foi possivel ler " + arquivo.toAbsolutePath(), erro);
        }
    }
}
