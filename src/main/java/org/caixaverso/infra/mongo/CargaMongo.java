package org.caixaverso.infra.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.caixaverso.infra.json.SementeJson;

import java.util.ArrayList;
import java.util.List;

public class CargaMongo {

    private final MongoDatabase database;

    public CargaMongo(MongoDatabase database) {
        this.database = database;
    }

    public String carregar() {
        MongoCollection<Document> contas = database.getCollection("conta");
        MongoCollection<Document> pessoas = database.getCollection("pessoa");
        contas.drop();
        pessoas.drop();

        List<Document> docsConta = new ArrayList<>();
        for (var item : SementeJson.contas().contas()) {
            docsConta.add(new Document("titular", item.titular()).append("saldo", item.saldo()));
        }
        contas.insertMany(docsConta);

        List<Document> docsPessoa = new ArrayList<>();
        for (var item : SementeJson.pessoas().pessoas()) {
            docsPessoa.add(new Document("nome", item.nome()).append("documento", item.documento()));
        }
        pessoas.insertMany(docsPessoa);

        return "Dados do JSON aplicados no Mongo: " + docsConta.size()
                + " contas e " + docsPessoa.size() + " pessoas.";
    }
}
