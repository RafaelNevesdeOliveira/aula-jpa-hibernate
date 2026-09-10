package org.caixaverso.exercicios;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.caixaverso.infra.banco.ContextoAula;
import org.caixaverso.infra.banco.TipoBanco;

public class Exercicio03ListarContasMongo implements Exercicio {

    @Override
    public String codigo() {
        return "03";
    }

    @Override
    public String titulo() {
        return "Listar contas no MongoDB (colecao conta)";
    }

    @Override
    public boolean aplicaA(TipoBanco tipo) {
        return tipo == TipoBanco.MONGO;
    }

    @Override
    public void executar(ContextoAula contexto) {
        MongoCollection<Document> contas = contexto.mongo().getCollection("conta");
        long total = contas.countDocuments();
        System.out.println("Total no Mongo: " + total);
        for (Document documento : contas.find()) {
            System.out.println(documento.getObjectId("_id")
                    + " | " + documento.getString("titular")
                    + " | " + documento.getString("saldo"));
        }
    }
}
