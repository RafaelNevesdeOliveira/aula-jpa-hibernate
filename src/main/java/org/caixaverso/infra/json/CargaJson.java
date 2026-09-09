package org.caixaverso.infra.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.caixaverso.model.Conta;
import org.caixaverso.model.Pessoa;
import org.caixaverso.repository.ContaRepository;
import org.caixaverso.repository.PessoaRepository;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CargaJson {

    private final ContaRepository contas;
    private final PessoaRepository pessoas;
    private final ObjectMapper mapper = new ObjectMapper();

    public CargaJson(ContaRepository contas, PessoaRepository pessoas) {
        this.contas = contas;
        this.pessoas = pessoas;
    }

    public static Path pasta() {
        return Path.of(System.getProperty("user.dir"), "data", "json");
    }

    public String carregar() {
        contas.apagarTodas();
        pessoas.apagarTodas();
        int totalContas = gravarContas();
        int totalPessoas = gravarPessoas();
        return "Dados do JSON aplicados: " + totalContas + " contas e " + totalPessoas + " pessoas.";
    }

    private int gravarContas() {
        ContasSeed seed = ler(pasta().resolve("contas.json"), ContasSeed.class);
        for (ContaSeed item : seed.contas()) {
            contas.salvar(new Conta(item.titular(), new BigDecimal(item.saldo())));
        }
        return seed.contas().size();
    }

    private int gravarPessoas() {
        PessoasSeed seed = ler(pasta().resolve("pessoas.json"), PessoasSeed.class);
        for (PessoaSeed item : seed.pessoas()) {
            pessoas.salvar(new Pessoa(item.nome(), item.documento()));
        }
        return seed.pessoas().size();
    }

    private <T> T ler(Path arquivo, Class<T> tipo) {
        if (!Files.exists(arquivo)) {
            throw new IllegalStateException("JSON nao encontrado: " + arquivo.toAbsolutePath());
        }
        try {
            return mapper.readValue(arquivo.toFile(), tipo);
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
