package org.caixaverso.infra.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.caixaverso.infra.banco.ConfiguracaoBanco;

public final class BancoMongo implements AutoCloseable {

    private final MongoClient cliente;
    private final MongoDatabase database;

    public BancoMongo(ConfiguracaoBanco config) {
        this.cliente = MongoClients.create(config.uriMongo());
        this.database = cliente.getDatabase(config.mongoDatabase());
    }

    public MongoDatabase database() {
        return database;
    }

    @Override
    public void close() {
        cliente.close();
    }
}
