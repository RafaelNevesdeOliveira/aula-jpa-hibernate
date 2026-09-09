package org.caixaverso.controller;

import org.caixaverso.model.Pessoa;
import org.caixaverso.repository.PessoaRepository;
import org.caixaverso.view.MenuView;
import org.caixaverso.view.PessoaView;

import java.util.Scanner;

public class PessoaController {

    private final PessoaRepository repository;
    private final PessoaView view;
    private final MenuView menu;

    public PessoaController(PessoaRepository repository, PessoaView view, MenuView menu) {
        this.repository = repository;
        this.view = view;
        this.menu = menu;
    }

    public void listar() {
        view.exibir(repository.listar());
    }

    public void cadastrar(Scanner entrada) {
        try {
            Pessoa pessoa = new Pessoa(view.lerNome(entrada), view.lerDocumento(entrada).strip());
            repository.salvar(pessoa);
            view.cadastrada(pessoa);
        } catch (RuntimeException erro) {
            menu.erro(erro.getMessage());
        }
    }
}
