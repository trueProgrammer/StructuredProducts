package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.persistence.entities.instrument.Product;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Vlad on 23.03.2016.
 */
public class ProductCsvToDbServiceTest {

    private final ProductCsvToDbService service = new ProductCsvToDbService();

    @Test
    public void importTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/products.csv");
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");

        List<ProductBean> csvBeans = service.readCsv(reader);
        Assert.assertThat(csvBeans, Matchers.notNullValue());
        Assert.assertThat(csvBeans, Matchers.hasSize(2));

        List<Product> products = service.convertToProdutsList(csvBeans);
        Assert.assertThat(products, Matchers.notNullValue());
        Assert.assertThat(csvBeans, Matchers.hasSize(2));

        Product product = products.get(0);
        Assert.assertThat(product.getName(), Matchers.is("Alpari"));
        Assert.assertThat(product.getMinTerm(), Matchers.is(1));
        Assert.assertThat(product.getInvestment().getMin(), Matchers.is(100));
    }

    @Test
    public void importTest2() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/bks_products.csv");
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");

        List<ProductBean> csvBeans = service.readCsv(reader);
        Assert.assertThat(csvBeans, Matchers.notNullValue());
        Assert.assertThat(csvBeans, Matchers.hasSize(6));

        List<Product> products = service.convertToProdutsList(csvBeans);
        Assert.assertThat(products, Matchers.notNullValue());
        Assert.assertThat(csvBeans, Matchers.hasSize(6));

        Product product = products.get(0);
        Assert.assertThat(product.getName(), Matchers.is("Нота №2"));
        Assert.assertThat(product.getProductType().getName(), Matchers.is("С участием (ограниченный риск)"));
        Assert.assertThat(product.getDescription(), Matchers.notNullValue());
        Assert.assertThat(product.getMaxTerm(), Matchers.is(30));
        Assert.assertThat(product.getMinTerm(), Matchers.is(0));
        Assert.assertThat(product.getUnderlayingList(), Matchers.hasSize(4));
        Assert.assertThat(product.getReturnValue(), Matchers.is(25.0F));
        Assert.assertThat(product.getStrategy().getName(), Matchers.is("Барьерные стратегии"));
        Assert.assertThat(product.getCurrency().getName(), Matchers.is("RUR"));
    }
}
