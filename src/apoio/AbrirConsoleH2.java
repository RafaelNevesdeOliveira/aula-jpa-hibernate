package apoio; // Console web do H2 para inspecionar o arquivo local.

import org.h2.tools.Server; // Servidor HTTP do próprio H2.

/**
 * Sobe o console em http://localhost:8082 para ver a tabela conta no arquivo local.
 * JDBC URL, usuário e senha são os mesmos de BancoLocal / unidade JPA "local".
 */
public class AbrirConsoleH2 {
    public static void main(String[] args) throws Exception { // Mantém o processo no ar.
        BancoLocal.garantirPasta(); // Garante data/h2 antes de abrir o servidor.
        Server web = Server.createWebServer("-webPort", "8082", "-ifNotExists").start();
        System.out.println("Console H2: " + web.getURL());
        System.out.println("JDBC URL: " + BancoLocal.url());
        System.out.println("Usuário: " + BancoLocal.USUARIO);
        System.out.println("Senha: (vazia)");
        System.out.println("Arquivo: " + BancoLocal.arquivoMv().toAbsolutePath());
        System.out.println("Deixe este main rodando e acesse o endereço no navegador.");
        Thread.currentThread().join(); // Não encerra até você parar a execução.
    }
}
