package com.structuredproducts.controllers.data;

/**
 * Created by Vlad on 08.01.2016.
 */
public enum ProductType {
    All("Все"),
    TYPE1("100% \u0437\u0430\u0449\u0438\u0442\u0430 \u043A\u0430\u043F\u0438\u0442\u0430\u043B\u0430 \u0431\u0435\u0437 \u0433\u0430\u0440\u0430\u043D\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u043D\u043E\u0439 \u0434\u043E\u0445\u043E\u0434\u043D\u043E\u0441\u0442\u0438"),
    TYPE2("\u0421 \u0443\u0447\u0430\u0441\u0442\u0438\u0435\u043C (\u043E\u0433\u0440\u0430\u043D\u0438\u0447\u0435\u043D\u043D\u044B\u0439 \u0440\u0438\u0441\u043A)"),
    TYPE3("\u0420\u0438\u0441\u043A\u043E\u0432\u044B\u0435");

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