package com.structuredproducts.persistence.entities.instrument;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="UNDERLAYING", schema = "INSTRUMENT")
public class Underlaying implements Serializable, UniqueWithName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @ManyToOne(targetEntity = UnderlayingType.class)
    @JoinColumn(name = "type")
    UnderlayingType type;

    /*@ManyToMany(mappedBy = "underlaying")
    private List<Product> products = Lists.newArrayList();*/

    /*public void setProducts(List<Product> products) {
        this.products = products;
    }


    public List<Product> getProducts() {
        return products;
    }*/

    public Underlaying(String name) {
        this.name = name;
    }
    public Underlaying() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnderlayingType getType() {
        return type;
    }

    public void setType(UnderlayingType type) {
        this.type = type;
    }
}
