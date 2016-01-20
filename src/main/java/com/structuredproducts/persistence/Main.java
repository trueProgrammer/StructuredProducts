package com.structuredproducts.persistence;

import com.structuredproducts.persistence.entities.instrument.Underlaying;
import com.structuredproducts.persistence.entities.instrument.UnderlayingType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("entityManager");
        EntityManager entityManager = factory.createEntityManager();
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            UnderlayingType type = new UnderlayingType();
            type.setName("Equity");
            entityManager.persist(type);

            Underlaying underlaying = new Underlaying();
            underlaying.setName("Russian");
            underlaying.setType(type);

            entityManager.persist(underlaying);

            transaction.commit();
        } finally {
            entityManager.close();
            factory.close();
        }


    }

}
