package org.caixaverso.infra.banco;

import com.mongodb.client.MongoDatabase;
import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.infra.mongo.BancoMongo;

public final class ContextoAula implements AutoCloseable {

    private final ConfiguracaoBanco config;
    private final EntityManagerFactory fabrica;
    private final BancoMongo mongo;

    public ContextoAula(ConfiguracaoBanco config, EntityManagerFactory fabrica, BancoMongo mongo) {
        this.config = config;
        this.fabrica = fabrica;
        this.mongo = mongo;
    }

    public ConfiguracaoBanco config() {
        return config;
    }

    public EntityManagerFactory jpa() {
        if (fabrica == null) {
            throw new IllegalStateException("Este exercicio e relacional. Use aula.banco=h2 ou postgres.");
        }
        return fabrica;
    }

    public MongoDatabase mongo() {
        if (mongo == null) {
            throw new IllegalStateException("Este exercicio e MongoDB. Use aula.banco=mongo.");
        }
        return mongo.database();
    }

    @Override
    public void close() {
        if (fabrica != null) {
            fabrica.close();
        }
        if (mongo != null) {
            mongo.close();
        }
    }
}
