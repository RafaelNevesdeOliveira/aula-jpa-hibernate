package org.caixaverso.view;

import org.caixaverso.exercicios.Exercicio;
import org.caixaverso.infra.h2.BancoH2;
import org.caixaverso.infra.json.CargaJson;

import java.util.List;
import java.util.Scanner;

public class MenuView {

    public void cabecalho(String carga, String urlConsole) {
        System.out.println();
        System.out.println("========================================");
        System.out.println(" Aula JPA + Hibernate");
        System.out.println("========================================");
        System.out.println("JSON:       " + CargaJson.pasta().toAbsolutePath());
        System.out.println("H2 arquivo: " + BancoH2.arquivoMv().toAbsolutePath());
        System.out.println("Console:    " + urlConsole);
        System.out.println("JDBC URL:   " + BancoH2.urlRelativa());
        System.out.println("Usuario:    " + BancoH2.USUARIO);
        System.out.println("Senha:      (vazia)");
        System.out.println(carga);
        System.out.println();
        System.out.println("O console ja esta no ar. Escolha um exercicio.");
    }

    public void exibirExercicios(List<Exercicio> exercicios) {
        System.out.println();
        System.out.println("=== Exercicios da aula ===");
        for (Exercicio exercicio : exercicios) {
            System.out.println(exercicio.codigo() + ") " + exercicio.titulo());
        }
        System.out.println("0) Sair");
        System.out.print("Exercicio: ");
    }

    public String lerOpcao(Scanner entrada) {
        String bruto = entrada.nextLine().strip().replaceAll("\\.$", "");
        if (bruto.matches("\\d")) {
            return "0" + bruto;
        }
        return bruto;
    }

    public void aviso(String mensagem) {
        System.out.println(mensagem);
    }

    public void erro(String mensagem) {
        System.out.println("Erro: " + mensagem);
    }

    public void aguardarVolta(Scanner entrada) {
        System.out.print("Enter para voltar ao catalogo...");
        entrada.nextLine();
    }
}
