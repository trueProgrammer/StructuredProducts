package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by Vlad on 20.02.2016.
 */
@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="TOP_PRODUCT", schema = "INSTRUMENT")
public class TopProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = ProductType.class)
    @JoinColumn(name = "type")
    TopType type;

    public TopProduct() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TopType getType() {
        return type;
    }

    public void setType(TopType type) {
        this.type = type;
    }
}
