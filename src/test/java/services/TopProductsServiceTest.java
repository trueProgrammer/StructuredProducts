package services;

import com.structuredproducts.data.ProductType;
import com.structuredproducts.data.TimeType;
import com.structuredproducts.data.TopProduct;
import com.structuredproducts.sevices.TopProductsService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vlad on 08.01.2016.
 */
public class TopProductsServiceTest {

    private TopProductsService topProductsService = new TopProductsService();

    @Test
    public void testGetAllProducts() {
        List<TopProduct> products = topProductsService.getTopProducts();
        Assert.assertTrue(products != null);
        Assert.assertTrue(products.size() == 24);
    }

    @Test
    public void testGetProductsByTimeAndType() {
        List<TopProduct> products = topProductsService.getTopProductsByTimeTypeAndProductType(TimeType.NEW, ProductType.TYPE1);
        Assert.assertTrue(products != null);
        Assert.assertTrue(products.size() == 4);

        products = topProductsService.getTopProductsByTimeTypeAndProductType(TimeType.MONTH, ProductType.TYPE2);
        Assert.assertTrue(products != null);
        Assert.assertTrue(products.size() == 4);

        products = topProductsService.getTopProductsByTimeTypeAndProductType(TimeType.MONTH, ProductType.All);
        Assert.assertTrue(products != null);
        Assert.assertTrue(products.size() == 5);
    }

    @Test
    public void test() {
        //System.out.println(Arrays.toString(TimeType.values()));
        System.out.println(ProductType.values()[0].getName());
    }

}
