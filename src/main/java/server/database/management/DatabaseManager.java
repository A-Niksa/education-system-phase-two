package server.database.management;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigKeyIdentifier;
import shareables.utils.config.ConfigManager;

public class DatabaseManager {
    private String hibernateConfigPath;
    private SessionFactory sessionFactory;

    public DatabaseManager() {
        hibernateConfigPath = getHibernateConfigPath();
        buildSessionFactory();
    }

    private void buildSessionFactory() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(hibernateConfigPath).build();
        sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
    }

    private String getHibernateConfigPath() {
        return ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, ConfigKeyIdentifier.HIBERNATE_CONFIG_PATH);
    }

    public void save(Object o) {
        Session session = startSession();
        session.save(o);
        endSession(session);
    }

    public void remove(Object o) {
        Session session = startSession();
        session.remove(o);
        endSession(session);
    }

    public <T> T fetch(Class<T> tClass, String id) {
        Session session = startSession();
        T t = session.get(tClass, id);
        endSession(session);
        return t;
    }

    private Session startSession() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }

    private void endSession(Session session) {
        session.getTransaction().commit(); // "flushing", so to speak
        session.close();
    }
}