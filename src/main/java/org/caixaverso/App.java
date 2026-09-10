package org.caixaverso;

import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.controller.ExercicioController;
import org.caixaverso.infra.banco.ConfiguracaoBanco;
import org.caixaverso.infra.h2.ConsoleH2;
import org.caixaverso.infra.jpa.JpaFactory;
import org.caixaverso.infra.jpa.LogsAula;
import org.caixaverso.infra.json.CargaJson;
import org.caixaverso.repository.ContaRepository;
import org.caixaverso.repository.PessoaRepository;
import org.caixaverso.view.MenuView;
import org.h2.tools.Server;

import java.util.Scanner;

public final class App implements AutoCloseable {

    private final ConfiguracaoBanco config;
    private final EntityManagerFactory fabrica;
    private final Server consoleH2;
    private final MenuView menu;
    private final ExercicioController exercicios;
    private final CargaJson carga;

    public App() {
        LogsAula.silenciarHibernate();
        this.config = ConfiguracaoBanco.carregar();
        this.consoleH2 = config.ehH2() ? ConsoleH2.iniciar() : null;
        this.fabrica = JpaFactory.abrir(config);
        this.menu = new MenuView();
        var contaRepository = new ContaRepository(fabrica);
        var pessoaRepository = new PessoaRepository(fabrica);
        this.exercicios = new ExercicioController(fabrica, menu);
        this.carga = new CargaJson(contaRepository, pessoaRepository);
    }

    public static void main(String[] args) {
        try (App app = new App()) {
            app.iniciar();
        }
    }

    public void iniciar() {
        menu.cabecalho(carga.carregar(), config, consoleH2);

        try (Scanner entrada = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                continuar = exercicios.executar(entrada);
            }
        }
    }

    @Override
    public void close() {
        fabrica.close();
        ConsoleH2.parar(consoleH2);
    }
}
