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
public class TopProduct{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product")
    Product product;

    public TopProduct() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
