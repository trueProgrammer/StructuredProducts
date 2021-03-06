package com.structuredproducts.persistence.entities.instrument;

import com.google.common.base.Joiner;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="employee")
@Table(name="PRODUCT", schema = "INSTRUMENT")
public class Product {

    private static final Joiner joiner = Joiner.on(", ");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToOne(targetEntity = ProductType.class)
    @JoinColumn(name = "productType")
    private ProductType productType;

    @Access(AccessType.PROPERTY)
    @ManyToMany
    @JoinTable(
        name = "INSTRUMENT.UNDERLAYINGS",
        joinColumns=@JoinColumn(name="product", referencedColumnName="id"),
        inverseJoinColumns=@JoinColumn(name="underlaying", referencedColumnName="id")
    )
    List<Underlaying> underlayingList;

    @Transient
    private String underlayings;

    @ManyToOne(targetEntity = Broker.class)
    @JoinColumn(name = "broker")
    private Broker broker;

    @Column(name = "return")
    private float returnValue;

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
    @JoinColumn(name = "input_currency")
    private Currency inputCurrency;

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "output_currency")
    private Currency outputCurrency;

    @ManyToOne(targetEntity = PaymentPeriodicity.class)
    @JoinColumn(name = "paymentPeriodicity")
    private PaymentPeriodicity paymentPeriodicity;

    @Column
    private String description;

    @Column
    private int minTerm;

    @Column
    private int maxTerm;

    @Column
    private int minInvest;

    @Column
    private int maxInvest;

    @Transient
    private RiskType riskType;

    @Transient
    private boolean top;

    public Product(){}

    public Product(Integer productId) {
        this.id = productId;
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

    public List<Underlaying> getUnderlayingList() {
        return underlayingList;
    }

    public void setUnderlayingList(List<Underlaying> underlaying) {
        underlayingList = underlaying;
        if(underlayingList != null) {
            Stream<Underlaying> underlayingStream = getUnderlayingList().parallelStream();
            underlayings = joiner.join(underlayingStream.map(Underlaying::getName).collect(Collectors.toList()));
        }
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public float getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(float returnValue) {
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

    public Currency getInputCurrency() {
        return inputCurrency;
    }

    public void setInputCurrency(Currency currency) {
        this.inputCurrency = currency;
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

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public boolean getTop() {
        return top;
    }

    public void setTop(boolean isTop) {
        this.top = isTop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(int minTerm) {
        this.minTerm = minTerm;
    }

    public int getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(int maxTerm) {
        this.maxTerm = maxTerm;
    }

    public String getUnderlayings() {
        return underlayings;
    }

    public void setUnderlayings(String underlayings) {
        this.underlayings = underlayings;
    }

    public int getMinInvest() {
        return minInvest;
    }

    public void setMinInvest(int minInvest) {
        this.minInvest = minInvest;
    }

    public int getMaxInvest() {
        return maxInvest;
    }

    public void setMaxInvest(int maxInvest) {
        this.maxInvest = maxInvest;
    }

    public Currency getOutputCurrency() {
        return outputCurrency;
    }

    public void setOutputCurrency(Currency outputCurrency) {
        this.outputCurrency = outputCurrency;
    }

    /**
     * This method compare two product and if they have some different params it can be saved to db
     */
    public boolean canBeSavedToDb(List<Product> products) {
        for (Product product : products) {
            if (!canBeSavedToDb(product)) {
                return false;
            }
        }
        return true;
    }

    public boolean canBeSavedToDb(Product product) {
        if (this == product) return false;

        if (Float.compare(product.returnValue, returnValue) != 0) return true;
        if (minTerm != product.minTerm) return true;
        if (maxTerm != product.maxTerm) return true;
        if (minInvest != product.minInvest) return true;
        if (maxInvest != product.maxInvest) return true;
        if (!inputCurrency.getName().equals(product.inputCurrency.getName())) return true;
        if (!outputCurrency.getName().equals(product.outputCurrency.getName())) return true;

        return false;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productType=" + productType +
                ", underlayingList=" + underlayingList +
                ", underlayings='" + underlayings + '\'' +
                ", broker=" + broker +
                ", returnValue=" + returnValue +
                ", strategy=" + strategy +
                ", legalType=" + legalType +
                ", payoff=" + payoff +
                ", risks=" + risks +
                ", inputCurrency=" + inputCurrency +
                ", outputCurrency=" + outputCurrency +
                ", paymentPeriodicity=" + paymentPeriodicity +
                ", description='" + description + '\'' +
                ", minTerm=" + minTerm +
                ", maxTerm=" + maxTerm +
                ", minInvest=" + minInvest +
                ", maxInvest=" + maxInvest +
                ", riskType=" + riskType +
                ", top=" + top +
                '}';
    }
}
