package org.caixaverso.view;

import org.caixaverso.model.Pessoa;

import java.util.List;
import java.util.Scanner;

public class PessoaView {

    public void exibir(List<Pessoa> pessoas) {
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }
        System.out.printf("%-6s %-20s %-12s%n", "ID", "NOME", "DOCUMENTO");
        for (Pessoa pessoa : pessoas) {
            System.out.printf("%-6s %-20s %-12s%n", pessoa.getId(), pessoa.getNome(), pessoa.getDocumento());
        }
    }

    public String lerNome(Scanner entrada) {
        System.out.print("Nome: ");
        return entrada.nextLine();
    }

    public String lerDocumento(Scanner entrada) {
        System.out.print("Documento (11 digitos): ");
        return entrada.nextLine();
    }

    public void cadastrada(Pessoa pessoa) {
        System.out.println("Pessoa gravada. id=" + pessoa.getId() + " nome=" + pessoa.getNome());
    }
}
