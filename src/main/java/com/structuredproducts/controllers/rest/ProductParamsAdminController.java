package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.persistence.entities.instrument.ProductParam;
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

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(AbstractAdminController.rootUrl)
public class ProductParamsAdminController extends AbstractAdminController{
    private final Logger logger = LoggerFactory.getLogger(ProductParamsAdminController.class);
    @RequestMapping(path="productparams/add",
            method= RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Message> addParam(@RequestBody String json) {
        logger.debug("Got json {}", json);

        Map<String, Object> map = null;
        try {
            map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            Integer productId = (Integer) map.get("product_id");
            String forecast = (String) map.get("forecast");
            String img = (String) map.get("img");
            String chart = (String) map.get("chart");

            Product product = new Product();
            product.setId(productId);

            ProductParam productParam = new ProductParam(product);
            productParam.setId(id);
            productParam.setChart(chart);
            productParam.setImg(img);
            productParam.setForecast(forecast);

            dbService.save(productParam);
        } catch (IOException e) {
            logger.error("can't handle json", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
