package org.caixaverso.infra.jpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.caixaverso.infra.h2.BancoH2;

import java.util.Map;

public final class JpaFactory {

    private JpaFactory() {
    }

    public static EntityManagerFactory abrirMemoria() {
        return Persistence.createEntityManagerFactory("laboratorio");
    }

    public static EntityManagerFactory abrirLocal() {
        BancoH2.garantirPasta();
        return Persistence.createEntityManagerFactory("local", Map.of(
                "jakarta.persistence.jdbc.url", BancoH2.url(),
                "jakarta.persistence.jdbc.user", BancoH2.USUARIO,
                "jakarta.persistence.jdbc.password", BancoH2.SENHA
        ));
    }
}
