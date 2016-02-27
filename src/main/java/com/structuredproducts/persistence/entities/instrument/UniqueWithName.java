package com.structuredproducts.persistence.entities.instrument;

public interface UniqueWithName extends DbObject{
    void setName(String name);
    String getName();
}
