package com.structuredproducts.controllers.data;

public class ProductBean {
    private long id;
    private String name;
    private String description;
    private String underlying;
    private int minInvestment;
    private String issuer;
    private int profit;
    private String strategy;
    private String legalType;
    private String payoff;
    private String risk;
    private String currency;
    private String periodicity;
    private String productType;
    private int maxInvestment;
    private int maxTerm;
    private int minTerm;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinInvestment() {
        return minInvestment;
    }

    public void setMinInvestment(int minInvestment) {
        this.minInvestment = minInvestment;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getLegalType() {
        return legalType;
    }

    public void setLegalType(String legalType) {
        this.legalType = legalType;
    }

    public String getPayoff() {
        return payoff;
    }

    public void setPayoff(String payoff) {
        this.payoff = payoff;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUnderlying() {
        return underlying;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }

    public int getProfit() {
        return profit;
    }

    public void setReturn(int profit) {
        this.profit = profit;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setMaxInvestment(int maxInvestment) {
        this.maxInvestment = maxInvestment;
    }

    public int getMaxInvestment() {
        return maxInvestment;
    }

    public int getMaxTerm() {
        return maxTerm;
    }

    public int getMinTerm() {
        return minTerm;
    }

    public void setMaxTerm(int maxTerm) {
        this.maxTerm = maxTerm;
    }

    public void setMinTerm(int minTerm) {
        this.minTerm = minTerm;
    }
}
