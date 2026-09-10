package org.caixaverso.infra.jpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.caixaverso.infra.banco.ConfiguracaoBanco;
import org.caixaverso.infra.h2.BancoH2;

import java.util.Map;

public final class JpaFactory {

    private JpaFactory() {
    }

    public static EntityManagerFactory abrirMemoria() {
        return Persistence.createEntityManagerFactory("laboratorio");
    }

    public static EntityManagerFactory abrirLocal() {
        return abrir(ConfiguracaoBanco.carregar());
    }

    public static EntityManagerFactory abrir(ConfiguracaoBanco config) {
        if (config.ehPostgres()) {
            return Persistence.createEntityManagerFactory("postgres", Map.of(
                    "jakarta.persistence.jdbc.url", config.urlPostgres(),
                    "jakarta.persistence.jdbc.user", config.usuario(),
                    "jakarta.persistence.jdbc.password", config.senha()
            ));
        }
        BancoH2.garantirPasta();
        return Persistence.createEntityManagerFactory("local", Map.of(
                "jakarta.persistence.jdbc.url", BancoH2.url(),
                "jakarta.persistence.jdbc.user", BancoH2.USUARIO,
                "jakarta.persistence.jdbc.password", BancoH2.SENHA
        ));
    }
}
