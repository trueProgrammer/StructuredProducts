package com.structuredproducts.persistence;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;

public class DBManager {

    @Autowired
    private EntityManagerFactory factory;

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public void close() {
        factory.close();
    }
}
