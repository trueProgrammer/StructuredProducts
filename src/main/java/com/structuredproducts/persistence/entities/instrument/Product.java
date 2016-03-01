package com.structuredproducts.persistence.entities.instrument;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="PRODUCT", schema = "INSTRUMENT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(targetEntity = ProductType.class)
    @JoinColumn(name = "productType")
    private ProductType productType;

    @ManyToOne(targetEntity = Term.class)
    @JoinColumn(name = "term")
    private Term term;

    @ManyToOne(targetEntity = Underlaying.class)
    @JoinColumn(name = "underlaying")
    private Underlaying underlaying;

    @ManyToOne(targetEntity = Investment.class)
    @JoinColumn(name = "investment")
    private Investment investment;

    @ManyToOne(targetEntity = Issuer.class)
    @JoinColumn(name = "issuer")
    private Issuer issuer;

    @ManyToOne(targetEntity = Return.class)
    @JoinColumn(name = "return")
    private Return returnValue;

    @ManyToOne(targetEntity = Strategy.class)
    @JoinColumn(name = "strategy")
    private Strategy strategy;

    @ManyToOne(targetEntity = LegalType.class)
    @JoinColumn(name = "legalType")
    private LegalType legalType;

    @ManyToOne(targetEntity = PayOff.class)
    @JoinColumn(name = "payoff")
    private PayOff payoff;

    @ManyToOne(targetEntity = Risks.class)
    @JoinColumn(name = "risks")
    private Risks risks;

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency")
    private Currency currency;

    @ManyToOne(targetEntity = PaymentPeriodicity.class)
    @JoinColumn(name = "paymentPeriodicity")
    private PaymentPeriodicity paymentPeriodicity;

    @Transient
    private RiskType riskType;

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Underlaying getUnderlaying() {
        return underlaying;
    }

    public void setUnderlaying(Underlaying underlaying) {
        this.underlaying = underlaying;
    }

    public Investment getInvestment() {
        return investment;
    }

    public void setInvestment(Investment investment) {
        this.investment = investment;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public Return getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Return returnValue) {
        this.returnValue = returnValue;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public LegalType getLegalType() {
        return legalType;
    }

    public void setLegalType(LegalType legalType) {
        this.legalType = legalType;
    }

    public PayOff getPayoff() {
        return payoff;
    }

    public void setPayoff(PayOff payoff) {
        this.payoff = payoff;
    }

    public Risks getRisks() {
        return risks;
    }

    public void setRisks(Risks risks) {
        this.risks = risks;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public PaymentPeriodicity getPaymentPeriodicity() {
        return paymentPeriodicity;
    }

    public void setPaymentPeriodicity(PaymentPeriodicity paymentPeriodicity) {
        this.paymentPeriodicity = paymentPeriodicity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }
}
