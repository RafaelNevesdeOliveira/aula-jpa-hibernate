package org.caixaverso.exercicios;

import jakarta.persistence.EntityManagerFactory;

public interface Exercicio {

    String codigo();

    String titulo();

    void executar(EntityManagerFactory fabrica);
}
