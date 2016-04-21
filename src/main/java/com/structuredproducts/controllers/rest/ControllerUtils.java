package com.structuredproducts.controllers.rest;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.persistence.entities.instrument.RiskType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vlad on 20.04.2016.
 */
public class ControllerUtils {

    private static final Joiner joiner = Joiner.on(", ");

    //stupid RiskTypeToProductType for association button type and product type by risk....
    public static BiMap<String, RiskType> RiskTypeToProductType = ImmutableBiMap.<String, RiskType>builder().
            put("100% защита капитала без гарантированной доходности", RiskType.High).
            put("С участием (ограниченный риск)", RiskType.Medium).
            put("100% защита капитала плюс гарантированная доходность", RiskType.Low).build();

    public static List<Product> getProducts(List<Product> result) {
        setRiskType(result);
        setUnderlayings(result);
        return result;
    }

    public static void setRiskType(List<Product> products) {
        products.parallelStream().forEach(a -> {
            if(a.getProductType() != null) a.setRiskType(RiskTypeToProductType.get(a.getProductType().getName()));
        });
    }

    public static void setUnderlayings(List<Product> products) {
        products.parallelStream().forEach(
                a -> a.setUnderlayings(joiner.join(a.getUnderlayingList().parallelStream().map(b -> b.getName()).collect(Collectors.toList())))
        );
    }

}
