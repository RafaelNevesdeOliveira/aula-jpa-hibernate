package org.caixaverso.repository;

import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.infra.jpa.Transacao;
import org.caixaverso.model.Conta;

import java.util.List;

public class ContaRepository {

    private final EntityManagerFactory fabrica;

    public ContaRepository(EntityManagerFactory fabrica) {
        this.fabrica = fabrica;
    }

    public List<Conta> listar() {
        return Transacao.consultar(fabrica, em ->
                em.createQuery("select c from Conta c order by c.id", Conta.class).getResultList());
    }

    public long contar() {
        return Transacao.consultar(fabrica, em ->
                em.createQuery("select count(c) from Conta c", Long.class).getSingleResult());
    }

    public Conta salvar(Conta conta) {
        Transacao.executar(fabrica, em -> em.persist(conta));
        return conta;
    }

    public void apagarTodas() {
        Transacao.executar(fabrica, em -> em.createQuery("delete from Conta").executeUpdate());
    }
}
