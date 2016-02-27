package com.structuredproducts.persistence.entities.instrument;

public interface UniqueWithMinMax extends DbObject{
    Integer getMax();
    void setMax(Integer max);
    
    Integer getMin();
    void setMin(Integer min);
}
