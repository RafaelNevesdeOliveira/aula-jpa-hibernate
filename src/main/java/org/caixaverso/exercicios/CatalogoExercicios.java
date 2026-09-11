package org.caixaverso.exercicios;

import java.util.List;
import java.util.Optional;

public final class CatalogoExercicios {

    private CatalogoExercicios() {
    }

    public static List<Exercicio> todos() {
        return List.of(
                new Exercicio01ObjetoMemoria(),
                new Exercicio02ListarContasJpa(),
                new Exercicio03FabricaContexto(),
                new Exercicio04ListarTitular()
        );
    }

    public static Optional<Exercicio> porCodigo(String codigo) {
        String normalizado = codigo.strip().replaceAll("\\.$", "");
        if (normalizado.matches("\\d")) {
            normalizado = "0" + normalizado;
        }
        String escolhido = normalizado;
        return todos().stream()
                .filter(exercicio -> exercicio.codigo().equals(escolhido))
                .findFirst();
    }
}
