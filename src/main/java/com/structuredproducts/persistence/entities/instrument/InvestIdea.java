package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.Date;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "employee")
@Table(name = "INVEST_IDEA", schema = "INSTRUMENT")
public class InvestIdea {
    private static final int MAX_PREVIEW_SIZE = 50;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Date addDate;

    @Column
    private Boolean mainPage;

    @Transient
    private String preview;

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

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Boolean getMainPage() {
        return mainPage;
    }

    public void setMainPage(Boolean mainPage) {
        this.mainPage = mainPage;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview() {
        this.preview = content.substring(0, Math.min(MAX_PREVIEW_SIZE, content.length()));
        if (preview.length() < content.length()) {
            preview += "...";
        }
    }
}
