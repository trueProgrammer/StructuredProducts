package com.structuredproducts.persistence.entities.instrument;

/**
 * Created by Vlad on 29.02.2016.
 */
public enum RiskType {
    High("High"),
    Medium("Medium"),
    Low("Low");

    private final String value;

    RiskType(String value) {
        this.value = value;
    }

    public static RiskType getRiskType(String str) {
        for(RiskType type : values()) {
            if(type.value.equals(str)) {
                return type;
            }
        }
        return null;
    }
}
