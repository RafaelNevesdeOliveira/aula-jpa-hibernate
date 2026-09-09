package apoio; // Infraestrutura compartilhada: um ponto só para abrir a fábrica.

import jakarta.persistence.EntityManagerFactory; // Fábrica de contextos JPA.
import jakarta.persistence.Persistence; // Lê META-INF/persistence.xml.

import java.util.Map; // Sobrescreve a URL do arquivo local.

/** Centraliza a criação da fábrica; quem abriu deve fechá-la. */
public final class Jpa { // final impede subclasses desta classe utilitária.
    private Jpa() { } // Impede instâncias; só utilizamos o método estático.

    /** Unidade em memória (create-drop). Cada execução começa vazia. */
    public static EntityManagerFactory abrir() {
        return Persistence.createEntityManagerFactory("laboratorio"); // Exercícios isolados.
    }

    /** Unidade em arquivo data/h2. JPA e JDBC compartilham as mesmas linhas. */
    public static EntityManagerFactory abrirLocal() {
        BancoLocal.garantirPasta(); // Cria data/h2 antes do Hibernate abrir o arquivo.
        return Persistence.createEntityManagerFactory("local", Map.of(
            "jakarta.persistence.jdbc.url", BancoLocal.url(), // Mesma URL do DriverManager.
            "jakarta.persistence.jdbc.user", BancoLocal.USUARIO,
            "jakarta.persistence.jdbc.password", BancoLocal.SENHA
        ));
    }
}
