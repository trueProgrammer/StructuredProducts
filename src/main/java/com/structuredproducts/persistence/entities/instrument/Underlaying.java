package com.structuredproducts.persistence.entities.instrument;

import org.apache.commons.lang3.builder.EqualsBuilder;
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
    @Column
    private String period;
    @ManyToOne(targetEntity = UnderlayingType.class)
    @JoinColumn(name = "type")
    UnderlayingType type;

    public Underlaying(String name) {
        this.name = name;
    }
    public Underlaying() {
    }

    public Underlaying(Integer id, String name, String officialName) {
        this.id = id;
        this.name = name;
        this.officialName = officialName;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Underlaying{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", officialName='" + officialName + '\'' +
                ", period='" + period + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Underlaying that = (Underlaying) o;

        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(id, that.id)
                .append(name, that.name)
                .append(officialName, that.officialName);
        return equalsBuilder.build();

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
