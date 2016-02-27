package com.structuredproducts.persistence.entities.instrument;

public interface UniqueWithInt extends DbObject{
    Integer getCount();
    void setCount(Integer count);
}
