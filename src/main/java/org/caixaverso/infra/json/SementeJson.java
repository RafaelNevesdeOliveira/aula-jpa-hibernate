package org.caixaverso.infra.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class SementeJson {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SementeJson() {
    }

    public static Path pasta() {
        return Path.of(System.getProperty("user.dir"), "data", "json");
    }

    public static ContasSeed contas() {
        return ler(pasta().resolve("contas.json"), ContasSeed.class);
    }

    public static PessoasSeed pessoas() {
        return ler(pasta().resolve("pessoas.json"), PessoasSeed.class);
    }

    private static <T> T ler(Path arquivo, Class<T> tipo) {
        if (!Files.exists(arquivo)) {
            throw new IllegalStateException("JSON nao encontrado: " + arquivo.toAbsolutePath());
        }
        try {
            return MAPPER.readValue(arquivo.toFile(), tipo);
        } catch (Exception erro) {
            throw new IllegalStateException("Falha ao ler " + arquivo.toAbsolutePath(), erro);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContasSeed(List<ContaSeed> contas) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContaSeed(String titular, String saldo) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PessoasSeed(List<PessoaSeed> pessoas) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PessoaSeed(String nome, String documento) {
    }
}
