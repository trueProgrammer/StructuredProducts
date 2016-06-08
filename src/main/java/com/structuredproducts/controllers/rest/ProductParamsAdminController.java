package com.structuredproducts.controllers.rest;

import com.google.common.collect.Lists;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.persistence.entities.instrument.ProductParam;
import com.structuredproducts.persistence.entities.instrument.Underlaying;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
//        logger.debug("Got json {}", json);

        Map<String, Object> map = null;
        try {
            map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            Integer productId = (Integer) map.get("product_id");
            String forecast = (String) map.get("forecast");
            String img = (String) map.get("img");
            String chart = (String) map.get("chart");

            ArrayList<HashMap<String, Object>> underlayings = (ArrayList<HashMap<String, Object>>) map.get("underlaying");
            List<Underlaying> parsedUnderlayings = underlayings.stream()
                                            .map(underlayingMap -> new Underlaying((Integer) underlayingMap.get("id"), (String) underlayingMap.get("name"), (String) underlayingMap.get("officialName")))
                                            .collect(Collectors.toList());


            Product product = dbService.getObjectByKey(Product.class, productId);

            ProductParam productParam = new ProductParam(product);
            productParam.setId(id);
            productParam.setChart(chart);
            productParam.setImg(img);
            productParam.setForecast(forecast);
            dbService.saveOrUpdateProductParam(productParam);

            if (!Lists.newArrayList(product.getUnderlayingList()).equals(Lists.newArrayList(parsedUnderlayings))) {
                product.setUnderlayingList(parsedUnderlayings);
                dbService.save(parsedUnderlayings);
                dbService.save(product);
            }
        } catch (IOException e) {
            logger.error("can't handle json", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path="productparams/underlaying",
                           method = RequestMethod.GET,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<List<Underlaying>> getUnderlayings() {
        return new ResponseEntity<>((List<Underlaying>) dbService.getResultList(Underlaying.class), HttpStatus.OK);
    }
}
