package com.structuredproducts.controllers.rest;

import com.google.common.collect.ImmutableMap;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.sevices.DBService;
import com.structuredproducts.sevices.ProductCsvToDbService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class AbstractAdminController {
    public static final String rootUrl = "/v1/admin";

    @Autowired
    protected DBService dbService;
    @Autowired
    protected ProductCsvToDbService converter;

    protected final Map<String, Class<?>> ENTITY_TYPES = ImmutableMap.<String, Class<?>>builder()
                                                                      .put("productType", ProductType.class)
                                                                      .put("term", Term.class)
                                                                      .put("investment", Investment.class)
                                                                      .put("issuer", Issuer.class)
                                                                      .put("return", Return.class)
                                                                      .put("strategy", Strategy.class)
                                                                      .put("legalType", LegalType.class)
                                                                      .put("payoff", PayOff.class)
                                                                      .put("risks", Risks.class)
                                                                      .put("currency", Currency.class)
                                                                      .put("underlayingType", UnderlayingType.class)
                                                                      .put("underlaying", Underlaying.class)
                                                                      .put("product", Product.class)
                                                                      .put("paymentPeriodicity", PaymentPeriodicity.class)
                                                                      .build();
}
