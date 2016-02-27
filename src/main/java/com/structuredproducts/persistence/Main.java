package com.structuredproducts.persistence;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.structuredproducts.persistence.entities.instrument.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        System.out.println(Currency.class.getSimpleName());
//        DBService dbService = new DBService();
//
//        //List<ProductType> list = (List<ProductType>) dbService.getResultList(ProductType.class);
//
//        ProductType p = new ProductType();
//        p.setName("qwerty!!");
//        List<ProductType> list = Lists.newArrayList(p);
//
//        dbService.saveList(list);
//
//        dbService.destroy();
    }

}

class DBService {

    private DBManager dbManager = new DBManager();

    private  final Map<Class<?>, String> TABLE_TO_TYPE_MAPPING = ImmutableMap.<Class<?>, String>builder().
            put(ProductType.class, "INSTRUMENT.PRODUCT_TYPE").
            put(Term.class, "INSTRUMENT.TERM").
            put(Investment.class, "INSTRUMENT.INVESTMENT").
            put(Issuer.class, "INSTRUMENT.ISSUER").
            put(Return.class, "INSTRUMENT.RETURN").
            build();

    public List<?> getResultList(Class<?> clazz) {
        Query query = dbManager.getEntityManager().createNativeQuery("SELECT * from " + TABLE_TO_TYPE_MAPPING.get(clazz), clazz);
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query.getResultList();
    }

    public void saveList(List<?> list) {
        EntityTransaction transaction = dbManager.getEntityManager().getTransaction();
        transaction.begin();
        try {
            for (Object obj : list) {
                dbManager.getEntityManager().merge(obj);
            }
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
        }
    }



    @PreDestroy
    public void destroy() {
        dbManager.close();
    }
}