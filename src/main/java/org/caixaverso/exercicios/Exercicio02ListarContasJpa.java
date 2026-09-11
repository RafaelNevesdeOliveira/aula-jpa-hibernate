package org.caixaverso.exercicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.model.Conta;

import java.util.List;

public class Exercicio02ListarContasJpa implements Exercicio {

    @Override
    public String codigo() {
        return "02";
    }

    @Override
    public String titulo() {
        return "Listar contas com JPA no H2 que ja esta aberto";
    }

    @Override
    public void executar(EntityManagerFactory fabrica) {
        EntityManager entityManager = fabrica.createEntityManager();

        try{
            List<Conta> contas= entityManager
                    .createQuery("select c from Conta c order by c.id", Conta.class)
                    .getResultList();
            System.out.println("Total no H2: " + contas.size());
            for(Conta conta : contas){
                System.out.println(conta.getId() + " | " + conta.getTitular() + " | " + conta.getSaldo());
            }
        }finally {
            entityManager.close();
        }
    }
}
