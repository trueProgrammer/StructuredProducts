package com.structuredproducts.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {

    private final EntityManagerFactory factory;
    private final EntityManager entityManager;


    public DBManager() {
        factory = Persistence.createEntityManagerFactory("entityManager");
        entityManager = factory.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void close() {
        entityManager.close();
        factory.close();
    }
}
