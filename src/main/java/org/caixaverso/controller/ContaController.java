package org.caixaverso.controller;

import org.caixaverso.model.Conta;
import org.caixaverso.repository.ContaRepository;
import org.caixaverso.view.ContaView;
import org.caixaverso.view.MenuView;

import java.math.BigDecimal;
import java.util.Scanner;

public class ContaController {

    private final ContaRepository repository;
    private final ContaView view;
    private final MenuView menu;

    public ContaController(ContaRepository repository, ContaView view, MenuView menu) {
        this.repository = repository;
        this.view = view;
        this.menu = menu;
    }

    public void listar() {
        view.exibir(repository.listar());
    }

    public void cadastrar(Scanner entrada) {
        try {
            Conta conta = new Conta(view.lerTitular(entrada), new BigDecimal(view.lerSaldo(entrada).strip()));
            repository.salvar(conta);
            view.cadastrada(conta);
        } catch (IllegalArgumentException | ArithmeticException erro) {
            menu.erro(erro.getMessage());
        }
    }
}
