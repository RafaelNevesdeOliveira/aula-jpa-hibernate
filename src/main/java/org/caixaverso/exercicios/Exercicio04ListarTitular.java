package org.caixaverso.exercicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.infra.jpa.JpaFactory;
import org.caixaverso.model.Conta;

public class Exercicio04ListarTitular implements Exercicio {

    @Override
    public String codigo() {
        return "04";
    }

    @Override
    public String titulo() {
        return "Retornar um titular";
    }

    @Override
    public void executar(EntityManagerFactory fabricaDaAula) {
        EntityManagerFactory fabrica = JpaFactory.abrirMemoria();
        try{
            Long id = Conta.gravar(fabrica, "Kenia", "10000.00");
            EntityManager em = fabrica.createEntityManager();

            try{
                Conta conta = em.find(Conta.class, id); //FIND select  titular from Conta where ID = ? = find(Conta.class, id
                System.out.println("Id confirmado: " +  id);
                System.out.println("Titular gravado: " + conta.getTitular());
            }finally {
                em.close();
            }
        }finally {
            fabrica.close();
        }

    }
}