package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.persistence.entities.instrument.TopProduct;
import com.structuredproducts.sevices.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(AbstractAdminController.rootUrl)
public class TopProductAdminController extends AbstractAdminController{
    Logger logger = LoggerFactory.getLogger(TopProductAdminController.class);

    @RequestMapping(path="/getTopProducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object[]> getTopProducts(@RequestParam(name="time") String time) {
        List<Product> products = (List<Product>) dbService.getResultList(Product.class);
        List<TopProduct> topProducts = (List<TopProduct>) dbService.getResultList(TopProduct.class);
        products.forEach(product -> {
            if (topProducts.stream().anyMatch(topProduct -> topProduct.getProduct().getId().equals(product.getId()) && topProduct.getTime().equals(time))) {
                product.setTop(true);
            } else {
                product.setTop(false);
            }
        });
        return new ResponseEntity<>(products.toArray(), HttpStatus.OK);
    }

    @RequestMapping(path="/addToTop",
                           method = RequestMethod.POST,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> addToTopProducts(@RequestBody String json) {
        logger.debug("Got json {}", json);

        try {
            List<TopProduct> products = extractTopProductsFromJson(json);
            dbService.save(products);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path="/removeFromTop",
                           method = RequestMethod.POST,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> removeFromTopProducts(@RequestBody String json) {
        logger.debug("Got json {}", json);
        try {
            List<TopProduct> products = extractTopProductsFromJson(json);
            dbService.removeTopProductByProduct(products);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<TopProduct> extractTopProductsFromJson(@RequestBody String json) throws IOException {
        Map<String, Object> map = ServiceUtils.getObjectMapping(json);
        List<Integer> ids = (List<Integer>) map.get("ids");
        String time = (String) map.get("time");
        List<TopProduct> products = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            TopProduct topProduct = new TopProduct();
            Product product = new Product();
            product.setId(ids.get(i));

            topProduct.setProduct(product);
            topProduct.setTime(time);
            products.add(topProduct);
        }
        return products;
    }
}
