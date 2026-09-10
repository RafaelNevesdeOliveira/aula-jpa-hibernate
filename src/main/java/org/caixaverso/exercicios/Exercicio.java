package org.caixaverso.exercicios;

import org.caixaverso.infra.banco.ContextoAula;
import org.caixaverso.infra.banco.TipoBanco;

public interface Exercicio {

    String codigo();

    String titulo();

    default boolean aplicaA(TipoBanco tipo) {
        return true;
    }

    void executar(ContextoAula contexto);
}
