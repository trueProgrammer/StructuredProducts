package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "system_property", schema = "INSTRUMENT")
public class SystemProperty {
    @Id
    @Column
    private String key;

    @Column
    private String value;

    public SystemProperty() {}

    public SystemProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
