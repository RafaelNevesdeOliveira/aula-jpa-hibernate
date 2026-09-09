package org.caixaverso.infra.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LogsAula {

    private LogsAula() {
    }

    public static void silenciarHibernate() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.orm").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.jpa").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.engine").setLevel(Level.SEVERE);
        Logger.getLogger("org.jboss.logging").setLevel(Level.SEVERE);
    }
}
