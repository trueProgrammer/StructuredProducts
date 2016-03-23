package com.structuredproducts.controllers.data;

import java.util.List;

public class ProductBean {
    private long id;
    private String name;
    private List<String> underlying;
    private String underlying2;
    private int minInvestment;
    private String broker;
    private double profit;
    private String strategy;
    private String legalType;
    private String payoff;
    private String risk;
    private String currency;
    private String periodicity;
    private String productType;
    private String description;
    private int maxInvestment;
    private int maxTerm;
    private int minTerm;

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

    public List<String> getUnderlying() {
        return underlying;
    }

    public double getProfit() {
        return profit;
    }

    public void setReturn(double profit) {
        this.profit = profit;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
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

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnderlying2() {
        return underlying2;
    }

    public void setUnderlying2(String underlying2) {
        this.underlying2 = underlying2;
    }

    public void setUnderlying(List<String> underlying) {
        this.underlying = underlying;
    }
}
