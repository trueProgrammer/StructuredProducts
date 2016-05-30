package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.persistence.entities.instrument.Product;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad on 13.05.2016.
 */
public class ProductCsvExportTest {

    private final ProductCsvToDbService service = new ProductCsvToDbService();

    @Test
    public void testExport() throws Exception {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/production_products.csv");
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");

        List<ProductBean> csvBeans = service.readCsv(reader);
        assertThat(csvBeans, notNullValue());
        assertThat(csvBeans, hasSize(1));

        List<Product> products = service.convertToProdutsList(csvBeans);

        String csv = service.convertToCsv(products);
        assertThat(csv, not(emptyOrNullString()));
    }
}
