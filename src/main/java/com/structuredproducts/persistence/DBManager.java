package com.structuredproducts.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {

    private final EntityManagerFactory factory;


    public DBManager() {
        factory = Persistence.createEntityManagerFactory("entityManager");
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public void close() {
        factory.close();
    }
}
