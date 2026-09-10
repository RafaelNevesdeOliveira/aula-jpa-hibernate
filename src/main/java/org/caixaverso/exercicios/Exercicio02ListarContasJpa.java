package org.caixaverso.exercicios;

import jakarta.persistence.EntityManager;
import org.caixaverso.infra.banco.ContextoAula;
import org.caixaverso.infra.banco.TipoBanco;
import org.caixaverso.model.Conta;

import java.util.List;

public class Exercicio02ListarContasJpa implements Exercicio {

    @Override
    public String codigo() {
        return "02";
    }

    @Override
    public String titulo() {
        return "Listar contas com JPA (H2 ou PostgreSQL)";
    }

    @Override
    public boolean aplicaA(TipoBanco tipo) {
        return tipo != TipoBanco.MONGO;
    }

    @Override
    public void executar(ContextoAula contexto) {
        EntityManager em = contexto.jpa().createEntityManager();
        try {
            List<Conta> contas = em.createQuery("select c from Conta c order by c.id", Conta.class)
                    .getResultList();
            System.out.println("Total no banco relacional: " + contas.size());
            for (Conta conta : contas) {
                System.out.println(conta.getId() + " | " + conta.getTitular() + " | " + conta.getSaldo());
            }
        } finally {
            em.close();
        }
    }
}
