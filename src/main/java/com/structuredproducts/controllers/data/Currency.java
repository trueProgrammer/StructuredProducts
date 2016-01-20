package com.structuredproducts.controllers.data;

/**
 * Created by Vlad on 08.01.2016.
 */
public enum Currency {

    RUR("RUR"),
    EUR("EUR"),
    USD("USD");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
