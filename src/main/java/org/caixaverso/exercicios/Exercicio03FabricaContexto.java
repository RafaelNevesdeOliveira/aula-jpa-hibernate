package org.caixaverso.exercicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.infra.jpa.JpaFactory;

public class Exercicio03FabricaContexto implements Exercicio {

    @Override
    public String codigo() {
        return "03";
    }

    @Override
    public String titulo() {
        return "Verificar pool de contexto";
    }

    @Override
    public void executar(EntityManagerFactory fabricaDaAula) {
        EntityManagerFactory fabrica = JpaFactory.abrirMemoria();
        try {
            EntityManager primeiro = fabrica.createEntityManager();
            EntityManager segundo = fabrica.createEntityManager();
            try {
                System.out.println("Contexto aberto: " + primeiro.isOpen());
                System.out.println("Mesma instancia: " + (primeiro == segundo));
            } finally {
                primeiro.close();
                segundo.close();
            }
        } finally {
            fabrica.close();
        }
    }
}
