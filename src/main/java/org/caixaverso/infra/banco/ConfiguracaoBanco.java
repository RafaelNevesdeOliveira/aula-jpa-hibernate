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
    private final String mongoHost;
    private final String mongoPorta;
    private final String mongoDatabase;

    private ConfiguracaoBanco(
            TipoBanco tipo,
            String host,
            String porta,
            String database,
            String usuario,
            String senha,
            String mongoHost,
            String mongoPorta,
            String mongoDatabase
    ) {
        this.tipo = tipo;
        this.host = host;
        this.porta = porta;
        this.database = database;
        this.usuario = usuario;
        this.senha = senha;
        this.mongoHost = mongoHost;
        this.mongoPorta = mongoPorta;
        this.mongoDatabase = mongoDatabase;
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
        return new ConfiguracaoBanco(
                tipoDe(tipoInformado),
                props.getProperty("postgres.host", "localhost"),
                props.getProperty("postgres.port", "5432"),
                props.getProperty("postgres.database", "aula11"),
                props.getProperty("postgres.user", "aula"),
                props.getProperty("postgres.password", "aula"),
                props.getProperty("mongo.host", "localhost"),
                props.getProperty("mongo.port", "27017"),
                props.getProperty("mongo.database", "aula11")
        );
    }

    public boolean ehH2() {
        return tipo == TipoBanco.H2;
    }

    public boolean ehPostgres() {
        return tipo == TipoBanco.POSTGRES;
    }

    public boolean ehMongo() {
        return tipo == TipoBanco.MONGO;
    }

    public boolean ehRelacional() {
        return ehH2() || ehPostgres();
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

    public String uriMongo() {
        return "mongodb://" + mongoHost + ":" + mongoPorta;
    }

    public String mongoDatabase() {
        return mongoDatabase;
    }

    private static TipoBanco tipoDe(String valor) {
        String normalizado = valor.strip().toLowerCase();
        if (normalizado.equals("postgres") || normalizado.equals("postgresql")) {
            return TipoBanco.POSTGRES;
        }
        if (normalizado.equals("mongo") || normalizado.equals("mongodb")) {
            return TipoBanco.MONGO;
        }
        return TipoBanco.H2;
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
