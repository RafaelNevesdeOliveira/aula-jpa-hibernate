package org.caixaverso;

import jakarta.persistence.EntityManagerFactory;
import org.caixaverso.controller.ExercicioController;
import org.caixaverso.infra.h2.BancoH2;
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

    private final EntityManagerFactory fabrica;
    private final Server consoleH2;
    private final MenuView menu;
    private final ExercicioController exercicios;
    private final CargaJson carga;

    public App() {
        LogsAula.silenciarHibernate();
        this.consoleH2 = ConsoleH2.iniciar();
        this.fabrica = JpaFactory.abrirLocal();
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
        String urlConsole = consoleH2 != null
                ? consoleH2.getURL()
                : "http://localhost:" + BancoH2.PORTA_CONSOLE + " (ja estava aberto)";
        menu.cabecalho(carga.carregar(), urlConsole);

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
