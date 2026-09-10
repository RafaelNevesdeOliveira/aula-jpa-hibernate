package org.caixaverso.infra.json;

import org.caixaverso.model.Conta;
import org.caixaverso.model.Pessoa;
import org.caixaverso.repository.ContaRepository;
import org.caixaverso.repository.PessoaRepository;

import java.math.BigDecimal;
import java.nio.file.Path;

public class CargaJson {

    private final ContaRepository contas;
    private final PessoaRepository pessoas;

    public CargaJson(ContaRepository contas, PessoaRepository pessoas) {
        this.contas = contas;
        this.pessoas = pessoas;
    }

    public static Path pasta() {
        return SementeJson.pasta();
    }

    public String carregar() {
        contas.apagarTodas();
        pessoas.apagarTodas();
        var seedContas = SementeJson.contas();
        for (var item : seedContas.contas()) {
            contas.salvar(new Conta(item.titular(), new BigDecimal(item.saldo())));
        }
        var seedPessoas = SementeJson.pessoas();
        for (var item : seedPessoas.pessoas()) {
            pessoas.salvar(new Pessoa(item.nome(), item.documento()));
        }
        return "Dados do JSON aplicados: " + seedContas.contas().size()
                + " contas e " + seedPessoas.pessoas().size() + " pessoas.";
    }
}
