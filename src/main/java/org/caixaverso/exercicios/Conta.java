package main.java.org.caixaverso.exercicios;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "conta")
public class Conta{
    @Id //chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String titular;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    protected Conta(){}

    public Conta(String titular, BigDecimal saldoInicial){
        renomear(titular);

        this.saldo = dinheiro(saldoInicial);
        if(saldo.signum() < 0 ){
            throw new IllegalArgumentException("Saldo Inicial negativo");
        }
    }

    public void renomear(String novoTitular){
        if(novoTitular == null || novoTitular.isBlank()){
            throw new IllegalArgumentException("Titular obrigatorio");
        }

        String nome = novoTitular.strip();// Remove espaços das extremidades
        if(nome.length() > 80){
            throw new IllegalArgumentException("Titular deve ter até 80 caracteres");
        }

        titular = nome;
    }

    private static BigDecimal dinheiro(BigDecimal valor){
        if(valor == null){
            throw new IllegalArgumentException("Valor obrigatorio");
        }

        try{
            BigDecimal normalizado = valor.setScale(2, RoundingMode.UNNECESSARY);
            if(normalizado.precision() > 15){
                throw new IllegalArgumentException("Valor excede precisao da coluna");
            }
            return normalizado;
        }catch (ArithmeticException error){
            throw new IllegalArgumentException("Use no maximo duas casas decimais", error);
        }
    }

    public void debitar(BigDecimal valor){
        BigDecimal quantia =  dinheiro(valor);
        if(quantia.signum() <= 0 || saldo.compareTo(quantia)< 0){
            throw new IllegalArgumentException("Débito inválido");
        }

        saldo = saldo.subtract(quantia);
    }

    public void creditar(BigDecimal valor){
        BigDecimal quantia =  dinheiro(valor);
        if(quantia.signum() <= 0){
            throw new IllegalArgumentException("Credito deve ser positivo");
        }

        saldo = dinheiro(saldo.add(quantia));
    }

    public Long getId(){ return id;}

    public String getTitular(){
        return titular;
    }

    public BigDecimal getSaldo(){
        return saldo;
    }


}