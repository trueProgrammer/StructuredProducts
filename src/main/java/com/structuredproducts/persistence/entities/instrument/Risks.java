package com.structuredproducts.persistence.entities.instrument;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="RISKS", schema = "INSTRUMENT")
public class Risks implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;

    public Risks() {
    }

    public Risks(String name) {
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
