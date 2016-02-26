package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="INVESTMENT", schema = "INSTRUMENT")
public class Investment implements Serializable, Nameble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer min;
    private Integer max;

    @Transient
    private String name;

    public Investment(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Investment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        if(min != null && max != null) {
            name = "От " + min + " до " + max + " рублей";
        } else if(min == null && max != null) {
            name = "До " +  max + " рублей";
        } else if (min != null && max == null) {
            name = "От " + min + " рублей";
        }
    }
}
