package org.caixaverso.view;

import org.caixaverso.exercicios.Exercicio;
import org.caixaverso.infra.banco.ConfiguracaoBanco;
import org.caixaverso.infra.h2.BancoH2;
import org.caixaverso.infra.json.CargaJson;
import org.h2.tools.Server;

import java.util.List;
import java.util.Scanner;

public class MenuView {

    public void cabecalho(String carga, ConfiguracaoBanco config, Server consoleH2) {
        System.out.println();
        System.out.println("========================================");
        System.out.println(" Aula JPA + Hibernate + Mongo");
        System.out.println("========================================");
        System.out.println("JSON:       " + CargaJson.pasta().toAbsolutePath());
        if (config.ehPostgres()) {
            System.out.println("Banco:      PostgreSQL");
            System.out.println("JDBC URL:   " + config.urlPostgres());
            System.out.println("Usuario:    " + config.usuario());
            System.out.println("Inspecao:   pgAdmin no banco aula11");
        } else if (config.ehMongo()) {
            System.out.println("Banco:      MongoDB");
            System.out.println("URI:        " + config.uriMongo());
            System.out.println("Database:   " + config.mongoDatabase());
            System.out.println("Inspecao:   Compass nas colecoes conta e pessoa");
        } else {
            String urlConsole = consoleH2 != null
                    ? consoleH2.getURL()
                    : "http://localhost:" + BancoH2.PORTA_CONSOLE + " (ja estava aberto)";
            System.out.println("Banco:      H2");
            System.out.println("H2 arquivo: " + BancoH2.arquivoMv().toAbsolutePath());
            System.out.println("Console:    " + urlConsole);
            System.out.println("JDBC URL:   " + BancoH2.urlRelativa());
            System.out.println("Usuario:    " + BancoH2.USUARIO);
            System.out.println("Senha:      (vazia)");
        }
        System.out.println(carga);
        System.out.println();
        System.out.println("Os dados ja vieram do JSON. Escolha um exercicio.");
    }

    public void exibirExercicios(List<Exercicio> exercicios) {
        System.out.println();
        System.out.println("=== Exercicios da aula ===");
        for (Exercicio exercicio : exercicios) {
            System.out.println(exercicio.codigo() + ") " + exercicio.titulo());
        }
        System.out.println("0) Sair");
        System.out.print("Exercicio: ");
    }

    public String lerOpcao(Scanner entrada) {
        String bruto = entrada.nextLine().strip().replaceAll("\\.$", "");
        if (bruto.matches("\\d")) {
            return "0" + bruto;
        }
        return bruto;
    }

    public void aviso(String mensagem) {
        System.out.println(mensagem);
    }

    public void erro(String mensagem) {
        System.out.println("Erro: " + mensagem);
    }

    public void aguardarVolta(Scanner entrada) {
        System.out.print("Enter para voltar ao catalogo...");
        entrada.nextLine();
    }
}
