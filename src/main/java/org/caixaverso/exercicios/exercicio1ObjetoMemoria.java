package main.java.org.caixaverso.exercicios;

import java.math.BigDecimal;

//OBjetivo: Criar uma conta em memoria e
// ver que new nao gera id nem grava no banco
public class exercicio1ObjetoMemoria {
    public static void main(String[] args){
        Conta conta =  new Conta("Ana", new BigDecimal("100.00"));

        System.out.println("Titular" +  conta.getTitular());
        System.out.println("Id antes de persistir" +  conta.getId());

    }
}
