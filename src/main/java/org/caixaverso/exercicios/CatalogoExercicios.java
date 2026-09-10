package org.caixaverso.exercicios;

import org.caixaverso.infra.banco.TipoBanco;

import java.util.List;
import java.util.Optional;

public final class CatalogoExercicios {

    private CatalogoExercicios() {
    }

    public static List<Exercicio> todos() {
        return List.of(
                new Exercicio01ObjetoMemoria(),
                new Exercicio02ListarContasJpa(),
                new Exercicio03ListarContasMongo()
        );
    }

    public static List<Exercicio> para(TipoBanco tipo) {
        return todos().stream().filter(exercicio -> exercicio.aplicaA(tipo)).toList();
    }

    public static Optional<Exercicio> porCodigo(String codigo, TipoBanco tipo) {
        String normalizado = codigo.strip().replaceAll("\\.$", "");
        if (normalizado.matches("\\d")) {
            normalizado = "0" + normalizado;
        }
        String escolhido = normalizado;
        return para(tipo).stream()
                .filter(exercicio -> exercicio.codigo().equals(escolhido))
                .findFirst();
    }
}
