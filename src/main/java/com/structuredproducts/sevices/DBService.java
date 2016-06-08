package com.structuredproducts.sevices;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.structuredproducts.persistence.entities.instrument.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DBService {

    private final static Logger logger = LoggerFactory.getLogger(DBService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private static final Map<Class<?>, String> TABLE_TO_TYPE_MAPPING = ImmutableMap.<Class<?>, String>builder().
            put(ProductType.class, "INSTRUMENT.PRODUCT_TYPE").
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
            put(InvestIdea.class, "INSTRUMENT.INVEST_IDEA").
            put(ProductParam.class, "INSTRUMENT.PRODUCT_PARAMS").
            put(SystemProperty.class, "INSTRUMENT.SYSTEM_PROPERTY").
            build();

    @Transactional
    public List<Product> getProductsByType(List<String> types) {
        Query query = createCacheableQuery("SELECT * from INSTRUMENT.PRODUCT p WHERE p.productType in (SELECT id FROM INSTRUMENT.PRODUCT_TYPE t WHERE t.riskType IN (:types))",
                Product.class);
        query.setParameter("types", types);
        return query.getResultList();
    }

    public List<?> getResultList(Class<?> clazz) {
        return createCacheableQuery("SELECT * from " + TABLE_TO_TYPE_MAPPING.get(clazz), clazz).getResultList();
    }

    private Query createCacheableQuery(String select, Class<?> clazz) {
        Query query = entityManager.createNativeQuery(select, clazz);
        query.setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query;
    }

    @Transactional
    public Object save(Object object) {
        return entityManager.merge(object);
    }

    @Transactional
    public boolean save(List<?> list) {
        for (Object obj : list) {
            entityManager.merge(obj);
        }
        entityManager.flush();
        return true;
    }

    @Transactional
    public void saveProducts(List<Product> products) {
        products.forEach(product -> {
            product.setCurrency(saveOrUpdateNameable(product.getCurrency()));
            product.setBroker(saveOrUpdateNameable(product.getBroker()));
            product.setLegalType(saveOrUpdateNameable(product.getLegalType()));
            product.setPaymentPeriodicity(saveOrUpdateNameable(product.getPaymentPeriodicity()));
            product.setProductType(saveOrUpdateNameable(product.getProductType()));
            product.setPayoff(saveOrUpdateNameable(product.getPayoff()));
            product.setReturnValue(product.getReturnValue());
            product.setRisks(saveOrUpdateNameable(product.getRisks()));
            product.setStrategy(saveOrUpdateNameable(product.getStrategy()));

            List<Underlaying> underlayings = Lists.newArrayList();
            product.getUnderlayingList().forEach(val -> {
                        val.setType(saveOrUpdateNameable(val.getType()));
                        underlayings.add(saveOrUpdateNameable(val));
                    }
            );
            product.getUnderlayingList().clear();
            product.setUnderlayingList(underlayings);

            logger.debug("Save all product contains.");
            entityManager.merge(product);
            logger.debug("Products saved");
        });
    }

    @Transactional
    public void saveOrUpdateProductParam(ProductParam productParam) {
        if (productParam == null || productParam == null) {
            return;
        }
        String queryStr = String.format("Select id from %s where product = :product", productParam.getClass().getSimpleName());
        TypedQuery<Integer> query = entityManager.createQuery(queryStr, Integer.class);
        query.setParameter("product", productParam.getProduct());
        List<Integer> ids = query.getResultList();
        if (ids.size() > 0) {
            productParam.setId(ids.get(0));
        }
        entityManager.merge(productParam);
    }

    @Transactional
    private <E extends UniqueWithName> E saveOrUpdateNameable(E obj) {
        if (obj == null || obj.getName() == null) {
            return null;
        }
        String queryStr = String.format("Select id from %s where name = :name", obj.getClass().getSimpleName());
        TypedQuery<Integer> query = entityManager.createQuery(queryStr, Integer.class);
        query.setParameter("name", obj.getName());
        List<Integer> ids = query.getResultList();
        if (ids.size() > 0) {
            obj.setId(ids.get(0));
            return obj;
        }
        return entityManager.merge(obj);
    }

    @Transactional
    public void remove(List<?> list) {
        for (Object obj : list) {
            obj = entityManager.merge(obj);
            entityManager.remove(obj);
        }
    }

    @Transactional
    public void removeTopProductByProduct(List<TopProduct> topProducts) {
        Query query = entityManager.createQuery("delete from TopProduct where product = :product_id");
        for (TopProduct topProduct : topProducts) {
            query.setParameter("product_id", topProduct.getProduct());
            query.executeUpdate();
        }
    }

    @Transactional
    public void remove(Object obj) {
        entityManager.remove(entityManager.contains(obj) ? obj : entityManager.merge(obj));
    }

    @Transactional
    public <S> S getObjectByKey(Class<S> clazz, Object id) {
        return entityManager.find(clazz, id);
    }

    @Transactional
    public List<Product> getTopProductsByTimeTypeAndProductType(String time, String productType) {
        TypedQuery<Product> productByTime;
        if (time == null) {
            productByTime = entityManager.createQuery("SELECT distinct product from TopProduct", Product.class);
        } else {
            productByTime = entityManager.createQuery("SELECT distinct product from TopProduct where time = :time", Product.class);
            productByTime.setParameter("time", time);
        }
        List<Product> topProducts = productByTime.getResultList();

        if (productType == null || productType.equals("Все")) {
            return topProducts;
        } else {
            return topProducts.stream()
                    .filter(p -> p.getProductType().getName().equals(productType))
                    .collect(Collectors.toList()); //filter by product type
        }
    }
}
