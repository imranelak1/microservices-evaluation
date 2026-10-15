package ma.projet.util;

import ma.projet.classes.*;
import ma.projet.beans.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Map;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/** Crée la factory MySQL partagée par les services instanciés par Spring. */
public final class HibernateUtil {
    private HibernateUtil() {}
    private static final Class<?>[] ENTITIES = {
        Categorie.class, Produit.class, Commande.class, LigneCommandeProduit.class,
        Employe.class, Projet.class, Tache.class, EmployeTache.class,
        Personne.class, Homme.class, Femme.class, Mariage.class
    };
    private static class Holder {
        static final SessionFactory FACTORY = build("evaluation", false);
    }
    public static SessionFactory getSessionFactory() { return Holder.FACTORY; }

    /** Les tests utilisent le même MySQL, avec un schéma recréé pour la suite. */
    public static SessionFactory build(String database, boolean test) {
        Properties properties = new Properties();
        try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) throw new IllegalStateException("Configuration introuvable");
            properties.load(input);
        } catch (IOException error) {
            throw new IllegalStateException("Lecture de configuration impossible", error);
        }
        Map<String, String> overrides = Map.of(
            "DB_URL", "hibernate.connection.url",
            "DB_USER", "hibernate.connection.username",
            "DB_PASSWORD", "hibernate.connection.password"
        );
        overrides.forEach((variable, property) -> {
            String value = System.getenv(variable);
            if (value != null) properties.setProperty(property, value);
        });
        if (test) properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        Configuration configuration = new Configuration().setProperties(properties);
        for (Class<?> entity : ENTITIES) configuration.addAnnotatedClass(entity);
        return configuration.buildSessionFactory();
    }
    public static void shutdown() { getSessionFactory().close(); }
}
