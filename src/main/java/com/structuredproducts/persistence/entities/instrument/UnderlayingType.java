package com.structuredproducts.persistence.entities.instrument;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="employee")
@Table(name="UNDERLAYING_TYPE", schema = "INSTRUMENT")
public class UnderlayingType implements Serializable{
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;

    public UnderlayingType() {
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
