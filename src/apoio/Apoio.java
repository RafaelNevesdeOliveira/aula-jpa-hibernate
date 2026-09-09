package apoio; // Helpers didáticos; leia esta classe antes dos exercícios de persistência.

import jakarta.persistence.EntityManager; // Contexto da unidade de trabalho.
import jakarta.persistence.EntityManagerFactory; // Fábrica recebida pelos helpers.
import main.java.org.caixaverso.exercicios.Conta; // Entidade da tabela conta.

import java.math.BigDecimal; // Saldo decimal.
import java.util.function.Consumer; // Ação que recebe o contexto e não devolve valor.

/** Evita repetir begin/commit/rollback e a gravação inicial de Ana. */
public final class Apoio {
    private Apoio() { } // Utilitário sem instâncias.

    /** Recebe condição/mensagem; lança AssertionError quando o resultado divergir. */
    public static void confere(boolean condicao, String mensagem) {
        if (!condicao) { // Impede que o exemplo anuncie sucesso incorretamente.
            throw new AssertionError(mensagem); // Não depende da opção -ea do Java.
        }
    }

    /** Recebe fábrica e ação; confirma a ação inteira ou desfaz e relança o erro. */
    public static void transacao(EntityManagerFactory fabrica, Consumer<EntityManager> acao) {
        var em = fabrica.createEntityManager(); // Novo contexto para esta operação.
        var tx = em.getTransaction(); // Transação pertencente a esse contexto.
        try {
            tx.begin(); // Inicia a unidade atômica.
            acao.accept(em); // Executa a lambda com o MESMO contexto.
            tx.commit(); // Confirma somente quando a ação termina sem erro.
        } catch (RuntimeException | Error erro) { // Inclui erros das verificações didáticas.
            if (tx.isActive()) { // Só desfaz se ainda existir transação ativa.
                tx.rollback(); // Desfaz inclusive alterações já sincronizadas por flush.
            }
            throw erro; // Preserva falha para o programa.
        } finally {
            em.close(); // Não reaproveita objetos alterados após rollback.
        }
    }

    /** Recebe fábrica/nome/saldo textual; devolve a chave após commit bem-sucedido. */
    public static Long gravar(EntityManagerFactory fabrica, String nome, String saldo) {
        Conta conta = new Conta(nome, new BigDecimal(saldo)); // Valida objeto e decimal exato.
        transacao(fabrica, em -> em.persist(conta)); // persist no contexto ativo; IDENTITY gera o ID.
        return conta.getId(); // Com IDENTITY o banco gerou a chave durante a inserção.
    }
}
