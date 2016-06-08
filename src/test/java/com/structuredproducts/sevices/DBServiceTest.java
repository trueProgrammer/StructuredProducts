package com.structuredproducts.sevices;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.structuredproducts.persistence.entities.instrument.*;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(value = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class DBServiceTest {

    @Autowired
    private IDatabaseTester tester;
    @Autowired
    private DBService dbService;

    @Before
    public void setUp() {

    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testGetProductList() {
        List<Product> resultList = (List<Product>) dbService.getResultList(Product.class);
        assertEquals("", 2, resultList.size());
    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testGetProductWithRelatedEntities() {
        Product product = dbService.getObjectByKey(Product.class, 4);
        assertNotNull(product);
        assertEquals(200000, product.getMinInvest());

        Broker broker = product.getBroker();
        assertNotNull(broker);
        assertEquals(2, (int)broker.getId());
        assertEquals("БКС", broker.getName());

        Strategy strategy = product.getStrategy();
        assertNotNull(strategy);
        assertEquals(1, (int)strategy.getId());
        assertEquals("Барьерные стратегии", strategy.getName());
    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testGetProductUnderlayingList() {
        Product product = dbService.getObjectByKey(Product.class, 5);
        assertNotNull(product);

        List<Underlaying> underlayings = product.getUnderlayingList();
        assertEquals(2, underlayings.size());
    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testGetProductUnderlayingListWithUnderlayingValues() {
        Product product = dbService.getObjectByKey(Product.class, 4);
        assertNotNull(product);

        List<Underlaying> underlayings = product.getUnderlayingList();
        assertEquals(1, underlayings.size());

        Underlaying underlaying = underlayings.get(0);
        assertEquals("Facebook", underlaying.getName());
        assertEquals("FACE", underlaying.getOfficialName());
    }

    @Ignore
    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testGetProductsByTwoTypes() {
        List<String> types = new ArrayList<>();
        types.add("С участием (ограниченный риск)");
        types.add("100% защита капитала плюс гарантированная доходность");
        List<Product> products = dbService.getProductsByType(types);
        List<ProductType> productTypes = (List<ProductType>) dbService.getResultList(ProductType.class);
        assertEquals(1, products.size());
    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testGetProductsByOneTypeIfThereIsNoProductWithThisType() {
        List<String> types = new ArrayList<>();
        types.add("Type 2");
        List<Product> products = dbService.getProductsByType(types);
        assertEquals(0, products.size());
    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testUnderlayingsRemovedWhenProductRemoved() throws Exception {
        Product product = new Product(4);
        dbService.remove(product);

        ITable underlayings_result = tester.getConnection().createQueryTable("UNDERLAYINGS_RESULT", "SELECT * FROM INSTRUMENT.UNDERLAYINGS");
        for (int i = 0; i < underlayings_result.getRowCount(); i++) {
            assertNotEquals(4, underlayings_result.getValue(i, "product"));
        }
    }

    @Test
    @DatabaseSetup("classpath:product_data.xml")
    public void testRemoveListOfProduct() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product(4));
        products.add(new Product(5));
        dbService.remove(products);
        assertTrue(dbService.getResultList(Product.class).isEmpty());
    }
}
