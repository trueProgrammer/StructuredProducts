package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="PAYMENT_PERIODICITY", schema = "INSTRUMENT")
public class PaymentPeriodicity implements Serializable, UniqueWithName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;

    public PaymentPeriodicity() {
    }

    public PaymentPeriodicity(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
