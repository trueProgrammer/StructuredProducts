package com.structuredproducts.persistence.entities.instrument;

/**
 * Created by Vlad on 06.06.2016.
 */
public enum UnderlayingPeriod {

    Y("y"),
    D5("5d");

    private final String value;

    UnderlayingPeriod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UnderlayingPeriod getUnderlayingInterval(String str) {
        for(UnderlayingPeriod type : values()) {
            if(type.value.equals(str)) {
                return type;
            }
        }
        return null;
    }

}
