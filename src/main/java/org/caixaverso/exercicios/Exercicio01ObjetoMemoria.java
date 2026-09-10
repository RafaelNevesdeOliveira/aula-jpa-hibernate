package org.caixaverso.exercicios;

import org.caixaverso.infra.banco.ContextoAula;
import org.caixaverso.model.Conta;

import java.math.BigDecimal;

public class Exercicio01ObjetoMemoria implements Exercicio {

    @Override
    public String codigo() {
        return "01";
    }

    @Override
    public String titulo() {
        return "Objeto em memoria (new nao gera id)";
    }

    @Override
    public void executar(ContextoAula contexto) {
        Conta conta = new Conta("Ana", new BigDecimal("100.00"));
        System.out.println("Titular: " + conta.getTitular());
        System.out.println("Id antes de persistir: " + conta.getId());
    }
}
