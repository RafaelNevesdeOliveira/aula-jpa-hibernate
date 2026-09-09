package org.caixaverso.view;

import org.caixaverso.model.Conta;

import java.util.List;
import java.util.Scanner;

public class ContaView {

    public void exibir(List<Conta> contas) {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }
        System.out.printf("%-6s %-20s %10s%n", "ID", "TITULAR", "SALDO");
        for (Conta conta : contas) {
            System.out.printf("%-6s %-20s %10s%n", conta.getId(), conta.getTitular(), conta.getSaldo());
        }
    }

    public String lerTitular(Scanner entrada) {
        System.out.print("Titular: ");
        return entrada.nextLine();
    }

    public String lerSaldo(Scanner entrada) {
        System.out.print("Saldo inicial (ex: 100.00): ");
        return entrada.nextLine();
    }

    public void cadastrada(Conta conta) {
        System.out.println("Conta gravada. id=" + conta.getId() + " titular=" + conta.getTitular());
    }
}
