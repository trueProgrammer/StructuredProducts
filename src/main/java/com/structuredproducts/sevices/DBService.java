package com.structuredproducts.sevices;

import com.google.common.collect.ImmutableMap;
import com.structuredproducts.persistence.DBManager;
import com.structuredproducts.persistence.entities.instrument.Currency;
import com.structuredproducts.persistence.entities.instrument.Investment;
import com.structuredproducts.persistence.entities.instrument.Issuer;
import com.structuredproducts.persistence.entities.instrument.LegalType;
import com.structuredproducts.persistence.entities.instrument.PayOff;
import com.structuredproducts.persistence.entities.instrument.PaymentPeriodicity;
import com.structuredproducts.persistence.entities.instrument.ProductType;
import com.structuredproducts.persistence.entities.instrument.Return;
import com.structuredproducts.persistence.entities.instrument.Risks;
import com.structuredproducts.persistence.entities.instrument.Strategy;
import com.structuredproducts.persistence.entities.instrument.Term;
import com.structuredproducts.persistence.entities.instrument.Underlaying;
import com.structuredproducts.persistence.entities.instrument.UnderlayingType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class DBService {

    private final static Logger logger = Logger.getLogger(DBService.class);
    @Autowired
    private DBManager dbManager;

    private static final Map<Class<?>, String> TABLE_TO_TYPE_MAPPING = ImmutableMap.<Class<?>, String>builder().
            put(ProductType.class, "INSTRUMENT.PRODUCT_TYPE").
            put(Term.class, "INSTRUMENT.TERM").
            put(Investment.class, "INSTRUMENT.INVESTMENT").
            put(Issuer.class, "INSTRUMENT.ISSUER").
            put(Return.class, "INSTRUMENT.RETURN").
            put(UnderlayingType.class, "INSTRUMENT.UNDERLAYING_TYPE").
            put(Underlaying.class, "INSTRUMENT.UNDERLAYING").
            put(Strategy.class, "INSTRUMENT.STRATEGY").
            put(LegalType.class, "INSTRUMENT.LEGAL_TYPE").
            put(PayOff.class, "INSTRUMENT.PAYOFF").
            put(Risks.class, "INSTRUMENT.RISKS").
            put(Currency.class, "INSTRUMENT.CURRENCY").
            put(PaymentPeriodicity.class, "INSTRUMENT.PAYMENT_PERIODICITY").
            build();

    public List<?> getResultList(Class<?> clazz) {
        Query query = dbManager.getEntityManager().createNativeQuery("SELECT * from " + TABLE_TO_TYPE_MAPPING.get(clazz), clazz);
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query.getResultList();
    }

    public void save(List<?> list) {
        EntityTransaction transaction = dbManager.getEntityManager().getTransaction();
        transaction.begin();
        try {
            for (Object obj : list) {
                dbManager.getEntityManager().merge(obj);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can not save list of entities.", e);
        } finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void remove(List<?> list) {
        EntityTransaction transaction = dbManager.getEntityManager().getTransaction();
        transaction.begin();
        try {
            for (Object obj : list) {
                obj = dbManager.getEntityManager().merge(obj);
                dbManager.getEntityManager().remove(obj);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can not remove list of entities.", e);
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
