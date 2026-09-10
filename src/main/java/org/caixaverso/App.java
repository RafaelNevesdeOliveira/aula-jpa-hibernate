package org.caixaverso;

import org.caixaverso.controller.ExercicioController;
import org.caixaverso.infra.banco.ConfiguracaoBanco;
import org.caixaverso.infra.banco.ContextoAula;
import org.caixaverso.infra.h2.ConsoleH2;
import org.caixaverso.infra.jpa.JpaFactory;
import org.caixaverso.infra.jpa.LogsAula;
import org.caixaverso.infra.json.CargaJson;
import org.caixaverso.infra.mongo.BancoMongo;
import org.caixaverso.infra.mongo.CargaMongo;
import org.caixaverso.repository.ContaRepository;
import org.caixaverso.repository.PessoaRepository;
import org.caixaverso.view.MenuView;
import org.h2.tools.Server;

import java.util.Scanner;

public final class App implements AutoCloseable {

    private final ConfiguracaoBanco config;
    private final ContextoAula contexto;
    private final Server consoleH2;
    private final MenuView menu;
    private final ExercicioController exercicios;
    private final String mensagemCarga;

    public App() {
        LogsAula.silenciarHibernate();
        this.config = ConfiguracaoBanco.carregar();
        this.consoleH2 = config.ehH2() ? ConsoleH2.iniciar() : null;
        if (config.ehMongo()) {
            var mongo = new BancoMongo(config);
            this.contexto = new ContextoAula(config, null, mongo);
            this.mensagemCarga = new CargaMongo(mongo.database()).carregar();
        } else {
            var fabrica = JpaFactory.abrir(config);
            this.contexto = new ContextoAula(config, fabrica, null);
            this.mensagemCarga = new CargaJson(
                    new ContaRepository(fabrica),
                    new PessoaRepository(fabrica)
            ).carregar();
        }
        this.menu = new MenuView();
        this.exercicios = new ExercicioController(contexto, menu);
    }

    public static void main(String[] args) {
        try (App app = new App()) {
            app.iniciar();
        }
    }

    public void iniciar() {
        menu.cabecalho(mensagemCarga, config, consoleH2);
        try (Scanner entrada = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                continuar = exercicios.executar(entrada);
            }
        }
    }

    @Override
    public void close() {
        contexto.close();
        ConsoleH2.parar(consoleH2);
    }
}
