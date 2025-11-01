package com.example.webjavahibernate.Utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.example.webjavahibernate.Models.*;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

                // configuration.addAnnotatedClass(Notebook.class);
                configuration.addAnnotatedClass(Seller.class);
                configuration.addAnnotatedClass(Customer.class);
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(Sale.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                System.out.println("Hibernate Java Config serviceRegistry created");

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                return sessionFactory;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}