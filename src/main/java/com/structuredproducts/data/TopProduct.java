package com.structuredproducts.data;

/**
 * Created by Vlad on 08.01.2016.
 */
public class TopProduct {

    private long id;
    private String name;
    private String bank;
    private Currency currency;
    private int invest;
    private double date;
    private int participation;
    private TimeType timeType;
    private ProductType productType;

    public TopProduct(){}

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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getInvest() {
        return invest;
    }

    public void setInvest(int invest) {
        this.invest = invest;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public int getParticipation() {
        return participation;
    }

    public void setParticipation(int participation) {
        this.participation = participation;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopProduct that = (TopProduct) o;

        if (id != that.id) return false;
        if (invest != that.invest) return false;
        if (Double.compare(that.date, date) != 0) return false;
        if (participation != that.participation) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (bank != null ? !bank.equals(that.bank) : that.bank != null) return false;
        if (currency != that.currency) return false;
        if (timeType != that.timeType) return false;
        return productType == that.productType;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (bank != null ? bank.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + invest;
        temp = Double.doubleToLongBits(date);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + participation;
        result = 31 * result + (timeType != null ? timeType.hashCode() : 0);
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", currency=" + currency +
                ", invest=" + invest +
                ", date=" + date +
                ", participation=" + participation +
                ", timeType=" + timeType +
                ", productType=" + productType +
                '}';
    }
}
