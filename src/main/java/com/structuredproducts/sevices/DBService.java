package com.structuredproducts.sevices;

import com.google.common.collect.ImmutableMap;
import com.structuredproducts.controllers.data.*;
import com.structuredproducts.persistence.DBManager;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.persistence.entities.instrument.Currency;
import com.structuredproducts.persistence.entities.instrument.ProductType;
import com.structuredproducts.persistence.entities.instrument.TopProduct;
import org.apache.log4j.Logger;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.CriteriaDeleteImpl;
import org.hibernate.jpa.criteria.CriteriaUpdateImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            put(Product.class, "INSTRUMENT.PRODUCT").
            put(Broker.class, "INSTRUMENT.BROKER").
            put(TopProduct.class, "INSTRUMENT.TOP_PRODUCT").
            build();

    public List<?> getProductsByType(List<String> types) {
        Query query = createCacheableQuery("SELECT * from INSTRUMENT.PRODUCT p WHERE p.productType in (SELECT id FROM INSTRUMENT.PRODUCT_TYPE t WHERE t.name IN (:types))",
                Product.class);
        query.setParameter("types", types);
        return query.getResultList();
    }
    public List<?> getResultList(Class<?> clazz) {
        return createCacheableQuery("SELECT * from " + TABLE_TO_TYPE_MAPPING.get(clazz), clazz)
                .getResultList();
    }

    private Query createCacheableQuery(String select, Class<?> clazz) {
        Query query = dbManager.getEntityManager().createNativeQuery(select, clazz);
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query;
    }

    public void save (Object object) {
        EntityTransaction transaction = dbManager.getEntityManager().getTransaction();
        transaction.begin();
        try {
            dbManager.getEntityManager().merge(object);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can not save list of entities.", e);
        } finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
        }
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

    public void saveProducts(List<Product> products) {
        EntityTransaction transaction = dbManager.getEntityManager().getTransaction();
        transaction.begin();
        logger.debug("Transaction " + transaction.getRollbackOnly());
        try {
            products.forEach(product -> {
                product.setCurrency(saveOrUpdateNameable(product.getCurrency()));
                product.setInvestment(saveOrUpdateUniqueWithMinMax(product.getInvestment()));
                product.setIssuer(saveOrUpdateNameable(product.getIssuer()));
                product.setLegalType(saveOrUpdateNameable(product.getLegalType()));
                product.setPaymentPeriodicity(saveOrUpdateNameable(product.getPaymentPeriodicity()));
                product.setProductType(saveOrUpdateNameable(product.getProductType()));
                product.setPayoff(saveOrUpdateNameable(product.getPayoff()));
                product.setReturnValue(saveOrUpdateUniqueWithInt(product.getReturnValue()));
                product.setRisks(saveOrUpdateNameable(product.getRisks()));
                product.setStrategy(saveOrUpdateNameable(product.getStrategy()));
                product.setTerm(saveOrUpdateUniqueWithMinMax(product.getTerm()));
                product.setUnderlaying(saveOrUpdateNameable(product.getUnderlaying()));
                logger.debug("save all product contains");
                dbManager.getEntityManager().merge(product);
                logger.debug("save product ");
            });
            transaction.commit();
        } catch (Exception e) {
            logger.error("Can not save list of products.", e);
        } finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    private <E extends UniqueWithName> E saveOrUpdateNameable(E obj) {
        String queryStr = String.format("Select id from %s where name = :name", obj.getClass().getSimpleName());
        TypedQuery<Integer> query = dbManager.getEntityManager().createQuery(queryStr, Integer.class);
        query.setParameter("name", obj.getName());
        List<Integer> ids = query.getResultList();
        if (ids.size() > 0) {
            obj.setId(ids.get(0));
            return obj;
        }
        return dbManager.getEntityManager().merge(obj);
    }

    private <E extends UniqueWithInt> E saveOrUpdateUniqueWithInt(E obj) {
        String queryStr = String.format("Select id from %s t where t.count = :count", obj.getClass().getSimpleName());
        TypedQuery<Integer> query = dbManager.getEntityManager().createQuery(queryStr, Integer.class);
        query.setParameter("count", obj.getCount());
        List<Integer> ids = query.getResultList();
        if (ids.size() > 0) {
            obj.setId(ids.get(0));
            return obj;
        }
        return dbManager.getEntityManager().merge(obj);
    }

    private <E extends UniqueWithMinMax> E saveOrUpdateUniqueWithMinMax(E obj) {
        String queryStr = String.format("Select id from %s t where t.min = :min and t.max = :max", obj.getClass().getSimpleName());
        TypedQuery<Integer> query = dbManager.getEntityManager().createQuery(queryStr, Integer.class);
        query.setParameter("min", obj.getMin());
        query.setParameter("max", obj.getMax());
        List<Integer> ids = query.getResultList();
        if (ids.size() > 0) {
            obj.setId(ids.get(0));
            return obj;
        }
        return dbManager.getEntityManager().merge(obj);

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

    public void removeTopProductByProduct(List<TopProduct> topProducts) {
        Query query = dbManager.getEntityManager().createQuery("delete from TopProduct where product = :product_id");

        EntityTransaction transaction = dbManager.getEntityManager().getTransaction();
        transaction.begin();
        try {
            for (TopProduct topProduct : topProducts) {
                query.setParameter("product_id", topProduct.getProduct());
                query.executeUpdate();
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

    public List<Product> getTopProductsByTimeTypeAndProductType(String time, String productType) {
        TypedQuery<Product> productByTime;
        if (time == null) {
            productByTime = dbManager.getEntityManager().createQuery("SELECT distinct product from TopProduct", Product.class);
        } else {
            productByTime = dbManager.getEntityManager().createQuery("SELECT distinct product from TopProduct where time = :time", Product.class);
            productByTime.setParameter("time", time);
        }
        List<Product> topProducts = productByTime.getResultList();

        if (productType == null || productType.equals("\u0412\u0441\u0435")) {
            return topProducts;
        } else {
            return topProducts.stream()
                    .filter(p -> p.getProductType().getName().equals(productType))
                    .collect(Collectors.toList()); //filter by product type
        }
    }
}
