package com.structuredproducts.persistence;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class EntityManagerBean {
    @Autowired
    private DBManager dbManager;

    private EntityManager em;

    public void init() {
        this.em = dbManager.getFactory().createEntityManager();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void destroy() {
        em.close();
    }
}
