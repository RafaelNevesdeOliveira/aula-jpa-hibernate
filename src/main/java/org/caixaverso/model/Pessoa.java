package org.caixaverso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, length = 11, unique = true)
    private String documento;

    protected Pessoa() {
    }

    public Pessoa(String nome, String documento) {
        this.nome = validarNome(nome);
        this.documento = validarDocumento(documento);
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome obrigatorio");
        }
        String valor = nome.strip();
        if (valor.length() > 80) {
            throw new IllegalArgumentException("Nome deve ter ate 80 caracteres");
        }
        return valor;
    }

    private static String validarDocumento(String documento) {
        if (documento == null || !documento.matches("\\d{11}")) {
            throw new IllegalArgumentException("Documento deve ter 11 digitos");
        }
        return documento;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }
}
