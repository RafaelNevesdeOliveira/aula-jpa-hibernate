package org.caixaverso.infra.h2;

import org.h2.tools.Server;

public final class ConsoleH2 {

    private ConsoleH2() {
    }

    public static Server iniciar() {
        BancoH2.garantirPasta();
        try {
            return Server.createWebServer("-webPort", String.valueOf(BancoH2.PORTA_CONSOLE), "-ifNotExists").start();
        } catch (Exception erro) {
            if (erro.getMessage() != null && erro.getMessage().contains("port")) {
                return null;
            }
            throw new IllegalStateException("Nao foi possivel iniciar o console H2", erro);
        }
    }

    public static void parar(Server servidor) {
        if (servidor != null) {
            servidor.stop();
        }
    }

    public static void main(String[] args) throws Exception {
        Server web = iniciar();
        if (web == null) {
            System.out.println("A porta " + BancoH2.PORTA_CONSOLE + " ja esta em uso. Abra http://localhost:" + BancoH2.PORTA_CONSOLE);
            return;
        }
        System.out.println("Console H2: " + web.getURL());
        System.out.println("JDBC URL:   " + BancoH2.urlRelativa());
        System.out.println("Usuario:    " + BancoH2.USUARIO);
        System.out.println("Senha:      (vazia)");
        Thread.currentThread().join();
    }
}
