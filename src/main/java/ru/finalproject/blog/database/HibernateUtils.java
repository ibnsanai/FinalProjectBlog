package ru.finalproject.blog.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private synchronized static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public synchronized static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public synchronized static void shutdown() {
        getSessionFactory().close();
    }

}
