package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="TERM", schema = "INSTRUMENT")
public class Term implements Serializable, Nameble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer min;
    private Integer max;

    @Transient
    private String name;

    public Term(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Term() {
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
        setName();
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
        setName();
    }

    public String getName() {
        return name;
    }

    public void setName() {
        if(min != null && max != null) {
            name = "От " + min + " до " + max + " месяцев";
        } else if(min == null && max != null) {
            name = "До " +  max + " месяцев";
        } else if (min != null && max == null) {
            name = "Свыше " + min + " месяцев";
        }
    }
}
