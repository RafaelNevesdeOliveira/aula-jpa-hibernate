package org.caixaverso.controller;

import org.caixaverso.exercicios.CatalogoExercicios;
import org.caixaverso.exercicios.Exercicio;
import org.caixaverso.infra.banco.ContextoAula;
import org.caixaverso.view.MenuView;

import java.util.Scanner;

public class ExercicioController {

    private final ContextoAula contexto;
    private final MenuView menu;

    public ExercicioController(ContextoAula contexto, MenuView menu) {
        this.contexto = contexto;
        this.menu = menu;
    }

    public boolean executar(Scanner entrada) {
        var tipo = contexto.config().tipo();
        menu.exibirExercicios(CatalogoExercicios.para(tipo));
        String codigo = menu.lerOpcao(entrada);
        if (codigo.equals("0") || codigo.equals("00") || codigo.isBlank()) {
            menu.aviso("Encerrando.");
            return false;
        }
        CatalogoExercicios.porCodigo(codigo, tipo).ifPresentOrElse(
                exercicio -> rodar(exercicio, entrada),
                () -> menu.erro("exercicio nao encontrado: " + codigo)
        );
        return true;
    }

    private void rodar(Exercicio exercicio, Scanner entrada) {
        System.out.println();
        System.out.println("--- Exercicio " + exercicio.codigo() + " — " + exercicio.titulo() + " ---");
        try {
            exercicio.executar(contexto);
        } catch (RuntimeException erro) {
            menu.erro(mensagemAmigavel(erro));
        }
        System.out.println("--- Fim ---");
        menu.aguardarVolta(entrada);
    }

    private static String mensagemAmigavel(RuntimeException erro) {
        if (erro instanceof NumberFormatException) {
            return "valor numerico invalido. Use exemplo: 100.00";
        }
        String mensagem = erro.getMessage();
        return mensagem == null || mensagem.isBlank() ? erro.getClass().getSimpleName() : mensagem;
    }
}
