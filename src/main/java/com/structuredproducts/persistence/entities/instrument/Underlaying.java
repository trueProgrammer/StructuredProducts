package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "employee")
@Table(name = "UNDERLAYING", schema = "INSTRUMENT")
public class Underlaying implements Serializable, UniqueWithName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String officialName;
    @ManyToOne(targetEntity = UnderlayingType.class)
    @JoinColumn(name = "type")
    UnderlayingType type;

    public Underlaying(String name) {
        this.name = name;
    }
    public Underlaying() {
    }

    public Underlaying(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Underlaying that = (Underlaying) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
