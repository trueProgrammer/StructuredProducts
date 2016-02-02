package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="employee")
@Table(name="PAYOFF", schema = "INSTRUMENT")
public class PayOff implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;

    public PayOff() {
    }

    public PayOff(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
