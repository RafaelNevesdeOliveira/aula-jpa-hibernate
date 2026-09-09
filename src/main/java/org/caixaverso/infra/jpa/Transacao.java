package org.caixaverso.infra.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Transacao {

    private Transacao() {
    }

    public static void executar(EntityManagerFactory fabrica, Consumer<EntityManager> acao) {
        consultar(fabrica, em -> {
            acao.accept(em);
            return null;
        });
    }

    public static <T> T consultar(EntityManagerFactory fabrica, Function<EntityManager, T> acao) {
        var em = fabrica.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            T resultado = acao.apply(em);
            tx.commit();
            return resultado;
        } catch (RuntimeException | Error erro) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw erro;
        } finally {
            em.close();
        }
    }
}
