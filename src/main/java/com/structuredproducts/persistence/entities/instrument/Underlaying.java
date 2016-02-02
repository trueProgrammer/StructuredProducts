package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="employee")
@Table(name="UNDERLAYING", schema = "INSTRUMENT")
public class Underlaying implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToOne(targetEntity = UnderlayingType.class)
    @JoinColumn(name = "type")
    UnderlayingType type;

    public Underlaying() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
