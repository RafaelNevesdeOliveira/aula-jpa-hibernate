package org.caixaverso.controller;

import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.exercicios.CatalogoExercicios;
import org.caixaverso.exercicios.Exercicio;
import org.caixaverso.view.MenuView;

import java.util.Scanner;

public class ExercicioController {

    private final EntityManagerFactory fabrica;
    private final MenuView menu;

    public ExercicioController(EntityManagerFactory fabrica, MenuView menu) {
        this.fabrica = fabrica;
        this.menu = menu;
    }

    public boolean executar(Scanner entrada) {
        menu.exibirExercicios(CatalogoExercicios.todos());
        String codigo = menu.lerOpcao(entrada);
        if (codigo.equals("0") || codigo.equals("00") || codigo.isBlank()) {
            menu.aviso("Encerrando.");
            return false;
        }
        CatalogoExercicios.porCodigo(codigo).ifPresentOrElse(
                exercicio -> rodar(exercicio, entrada),
                () -> menu.erro("exercicio nao encontrado: " + codigo)
        );
        return true;
    }

    private void rodar(Exercicio exercicio, Scanner entrada) {
        System.out.println();
        System.out.println("--- Exercicio " + exercicio.codigo() + " — " + exercicio.titulo() + " ---");
        try {
            exercicio.executar(fabrica);
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
