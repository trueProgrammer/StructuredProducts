package com.structuredproducts.data;

/**
 * Created by Vlad on 08.01.2016.
 */
public enum ProductType {
    All("Все"),
    TYPE1("Тип 1"),
    TYPE2("Тип 2");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProductType getEnum(String value) {
        for(ProductType v : values())
            if(v.getName().equalsIgnoreCase(value)) {
                return v;
            }
        throw new IllegalArgumentException();
    }

}