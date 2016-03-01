package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "employee")
@Table(name = "INVEST_IDEA", schema = "INSTRUMENT")
public class InvestIdea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(targetEntity = Broker.class)
    @JoinColumn(name = "broker")
    Broker broker;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }
}
