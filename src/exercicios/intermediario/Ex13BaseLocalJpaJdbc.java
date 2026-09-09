package exercicios.intermediario; // Compara JPA e JDBC no mesmo arquivo H2 local.

import apoio.Apoio; // persist com transação.
import apoio.BancoLocal; // URL, pasta e conexão JDBC do arquivo.
import apoio.Jpa; // abrirLocal() aponta para data/h2.

import java.math.BigDecimal; // Saldo gravado pelos dois lados.
import java.sql.Connection; // Leitura e escrita JDBC manuais.
import java.sql.PreparedStatement; // INSERT/SELECT com parâmetro.
import java.sql.ResultSet; // Cursor das linhas lidas pelo JDBC.
import java.sql.Statement; // RETURN_GENERATED_KEYS no insert JDBC.

/**
 * OBJETIVO: gravar no H2 em arquivo com JDBC e com JPA e ler o mesmo resultado dos dois lados.
 * CONTEXTO: o arquivo data/h2/laboratorio.mv.db sobrevive ao fechar o programa.
 */
public class Ex13BaseLocalJpaJdbc {
    public static void main(String[] args) throws Exception { // Propaga SQLException do JDBC.
        var fabrica = Jpa.abrirLocal(); // Abre (ou cria) data/h2/laboratorio.mv.db e a tabela conta.
        try {
            System.out.println("Arquivo local: " + BancoLocal.arquivoMv().toAbsolutePath());

            Long idJdbc = inserirViaJdbc("Caio", new BigDecimal("15.00")); // SQL explícito.
            Long idJpa = Apoio.gravar(fabrica, "Duda", "40.00"); // persist + commit; Hibernate gera INSERT.

            int totalJdbc = contarViaJdbc(); // SELECT count(*) escrito à mão.
            int totalJpa;
            var em = fabrica.createEntityManager(); // JPQL: sem SQL de tabela.
            try {
                totalJpa = em.createQuery("select count(c) from Conta c", Long.class)
                    .getSingleResult()
                    .intValue();
            } finally {
                em.close(); // Não deixa o contexto aberto até o fim do main.
            }

            System.out.println("Via JDBC: id=" + idJdbc + " Caio | 15.00");
            System.out.println("Via JPA: id=" + idJpa + " Duda | 40.00");
            System.out.println("Total JDBC: " + totalJdbc);
            System.out.println("Total JPA: " + totalJpa);
            System.out.println("JDBC e JPA leram o mesmo total: " + (totalJdbc == totalJpa));
            listarViaJdbc(); // Mostra todas as linhas já salvas no arquivo.
        } finally {
            fabrica.close(); // Fecha o pool; o arquivo .mv.db permanece no disco.
        }
    }

    /** INSERT JDBC na tabela conta; devolve a chave gerada pelo H2. */
    private static Long inserirViaJdbc(String titular, BigDecimal saldo) throws Exception {
        String sql = "insert into conta (titular, saldo) values (?, ?)"; // Colunas iguais às da entidade.
        try (Connection conexao = BancoLocal.abrirConexao();
             PreparedStatement inserir = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            inserir.setString(1, titular); // Parâmetro; não concatena o nome no SQL.
            inserir.setBigDecimal(2, saldo); // Mesmo tipo da coluna numeric(15,2).
            inserir.executeUpdate();
            try (ResultSet chaves = inserir.getGeneratedKeys()) { // IDENTITY do H2.
                chaves.next();
                return chaves.getLong(1);
            }
        }
    }

    /** COUNT escrito em SQL; deve coincidir com o count JPQL. */
    private static int contarViaJdbc() throws Exception {
        try (Connection conexao = BancoLocal.abrirConexao();
             PreparedStatement consultar = conexao.prepareStatement("select count(*) from conta");
             ResultSet resultado = consultar.executeQuery()) {
            resultado.next();
            return resultado.getInt(1); // Mapeamento manual da primeira coluna.
        }
    }

    /** Lista o arquivo inteiro — inclui execuções anteriores. */
    private static void listarViaJdbc() throws Exception {
        try (Connection conexao = BancoLocal.abrirConexao();
             PreparedStatement consultar = conexao.prepareStatement(
                 "select id, titular, saldo from conta order by id");
             ResultSet resultado = consultar.executeQuery()) {
            System.out.println("Linhas no arquivo:");
            while (resultado.next()) { // Cada next() é uma linha; JPA faria getResultList().
                System.out.println("  " + resultado.getLong("id")
                    + " | " + resultado.getString("titular")
                    + " | " + resultado.getBigDecimal("saldo"));
            }
        }
    }
}
