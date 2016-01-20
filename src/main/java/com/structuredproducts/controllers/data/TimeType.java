package com.structuredproducts.controllers.data;

/**
 * Created by Vlad on 08.01.2016.
 */
public enum TimeType {
    WEEK("За неделю"),
    MONTH("За Месяц"),
    NEW("Новые");

    private final String name;

    TimeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TimeType getEnum(String value) {
        for(TimeType v : values())
            if(v.getName().equalsIgnoreCase(value)) {
                return v;
            }
        throw new IllegalArgumentException();
    }
}
