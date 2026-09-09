package org.caixaverso.repository;

import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.infra.jpa.Transacao;
import org.caixaverso.model.Pessoa;

import java.util.List;

public class PessoaRepository {

    private final EntityManagerFactory fabrica;

    public PessoaRepository(EntityManagerFactory fabrica) {
        this.fabrica = fabrica;
    }

    public List<Pessoa> listar() {
        return Transacao.consultar(fabrica, em ->
                em.createQuery("select p from Pessoa p order by p.id", Pessoa.class).getResultList());
    }

    public long contar() {
        return Transacao.consultar(fabrica, em ->
                em.createQuery("select count(p) from Pessoa p", Long.class).getSingleResult());
    }

    public Pessoa salvar(Pessoa pessoa) {
        Transacao.executar(fabrica, em -> em.persist(pessoa));
        return pessoa;
    }

    public void apagarTodas() {
        Transacao.executar(fabrica, em -> em.createQuery("delete from Pessoa").executeUpdate());
    }
}
