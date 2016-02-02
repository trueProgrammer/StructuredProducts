package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="employee")
@Table(name="TERM", schema = "INSTRUMENT")
public class Term implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private int min;
    private int max;

    public Term() {
    }

    public Term(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }


}
