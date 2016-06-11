package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.persistence.entities.instrument.Product;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad on 23.03.2016.
 */
public class ProductCsvToDbServiceTest {

    private final ProductCsvToDbService service = new ProductCsvToDbService();

    @Test
    public void importFirstProductsTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/products.csv");
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");

        List<ProductBean> csvBeans = service.readCsv(reader, null);
        assertThat(csvBeans, notNullValue());
        assertThat(csvBeans, hasSize(2));

        List<Product> products = service.convertToProdutsList(csvBeans);
        assertThat(products, notNullValue());
        assertThat(csvBeans, hasSize(2));

        Product product = products.get(0);
        assertThat(product.getName(), is("Alpari"));
        assertThat(product.getMinTerm(), is(1));
        assertThat(product.getMinInvest(), is(100));
    }

    @Test
    public void importProductionProductsTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/production_products.csv");
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");

        List<ProductBean> csvBeans = service.readCsv(reader, null);
        assertThat(csvBeans, notNullValue());
        assertThat(csvBeans, hasSize(1));

        List<Product> products = service.convertToProdutsList(csvBeans);
        assertThat(products, notNullValue());
        assertThat(csvBeans, hasSize(1));

        Product product = products.get(0);
        assertThat(product.getName(), is("Золотая защита"));
        assertThat(product.getBroker().getName(), is("Открытие"));
        assertThat(product.getProductType().getName(), is("100% защита капитала без гарантированной доходности"));
        assertThat(product.getDescription(), not(emptyOrNullString()));
        assertThat(product.getMinTerm(), is(9));
        assertThat(product.getMaxTerm(), is(21));
        assertThat(product.getMinInvest(), is(250000));
        assertThat(product.getStrategy().getName(), is("Рост цены базового актива"));
        assertThat(product.getReturnValue(), is(25.0f));
        assertThat(product.getCurrency().getName(), is("RUR"));
        assertThat(product.getUnderlayingList().size(), is(4));
        assertThat(product.getUnderlayingList().get(0).getType().getName(), is("Акции"));
    }

    @Test
    public void importBksProductTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/bks_products.csv");
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");

        List<ProductBean> csvBeans = service.readCsv(reader, null);
        assertThat(csvBeans, notNullValue());
        assertThat(csvBeans, hasSize(6));

        List<Product> products = service.convertToProdutsList(csvBeans);
        assertThat(products, notNullValue());
        assertThat(csvBeans, hasSize(6));

        Product product = products.get(0);
        assertThat(product.getName(), is("Нота №2"));
        assertThat(product.getProductType().getName(), is("С участием (ограниченный риск)"));
        assertThat(product.getDescription(), notNullValue());
        assertThat(product.getMaxTerm(), is(30));
        assertThat(product.getMinTerm(), is(0));
        assertThat(product.getUnderlayingList(), hasSize(4));
        assertThat(product.getReturnValue(), is(25.0F));
        assertThat(product.getStrategy().getName(), is("Барьерные стратегии"));
        assertThat(product.getCurrency().getName(), is("RUR"));
    }
}
