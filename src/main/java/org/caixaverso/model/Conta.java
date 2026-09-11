package org.caixaverso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.caixaverso.infra.jpa.Transacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String titular;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    protected Conta() {
    }

    public Conta(String titular, BigDecimal saldoInicial) {
        renomear(titular);
        this.saldo = dinheiro(saldoInicial);
        if (saldo.signum() < 0) {
            throw new IllegalArgumentException("Saldo inicial negativo");
        }
    }

    public void renomear(String novoTitular) {
        if (novoTitular == null || novoTitular.isBlank()) {
            throw new IllegalArgumentException("Titular obrigatorio");
        }
        String nome = novoTitular.strip();
        if (nome.length() > 80) {
            throw new IllegalArgumentException("Titular deve ter ate 80 caracteres");
        }
        titular = nome;
    }

    public void debitar(BigDecimal valor) {
        BigDecimal quantia = dinheiro(valor);
        if (quantia.signum() <= 0 || saldo.compareTo(quantia) < 0) {
            throw new IllegalArgumentException("Debito invalido");
        }
        saldo = saldo.subtract(quantia);
    }

    public void creditar(BigDecimal valor) {
        BigDecimal quantia = dinheiro(valor);
        if (quantia.signum() <= 0) {
            throw new IllegalArgumentException("Credito deve ser positivo");
        }
        saldo = dinheiro(saldo.add(quantia));
    }

    private static BigDecimal dinheiro(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor obrigatorio");
        }
        try {
            BigDecimal normalizado = valor.setScale(2, RoundingMode.UNNECESSARY);
            if (normalizado.precision() > 15) {
                throw new IllegalArgumentException("Valor excede precisao da coluna");
            }
            return normalizado;
        } catch (ArithmeticException erro) {
            throw new IllegalArgumentException("Use no maximo duas casas decimais", erro);
        }
    }

    public static Long gravar(EntityManagerFactory fabrica, String titular, String saldo) {
        Conta conta = new Conta(titular, new BigDecimal(saldo));
        Transacao.executar(fabrica, em -> em.persist(conta));
        return conta.getId();
    }

    public Long getId() {
        return id;
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
