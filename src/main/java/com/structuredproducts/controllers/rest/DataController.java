package com.structuredproducts.controllers.rest;

import com.google.common.collect.*;
import com.structuredproducts.controllers.data.*;
import com.structuredproducts.controllers.data.InvestIdea;
import com.structuredproducts.controllers.data.ProductType;
import com.structuredproducts.controllers.data.TopProduct;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.sevices.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Vlad on 23.11.2015.
 */
@Controller
@RequestMapping("/v1/data")
public class DataController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private NewsService newsService;
    @Autowired
    private TopProductsService topProductsService;
    @Autowired
    private InvestIdeasService investIdeasService;
    @Autowired
    private DBService dbService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Data> getData() {
        logger.trace("Get data");
        logger.error("Get data");
        return new ResponseEntity<>(new Data("test"), HttpStatus.OK);
    }

    @RequestMapping(path = "/timetypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String[]> getTimeTypes() {
        TimeType[] timeTypes = TimeType.values();
        String[] values = new String[timeTypes.length];
        int i = 0;
        for(TimeType timeType : timeTypes) {
            values[i++] = timeType.getName();
        }

        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @RequestMapping(path = "/producttypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String[]> getProductTypes() {
        ProductType[] productTypes = ProductType.values();
        String[] values = new String[productTypes.length];
        int i = 0;
        for(ProductType productType : productTypes) {
            values[i++] = productType.getName();
        }

        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @RequestMapping(path = "/news",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<News[]> getNews() {
        List<News> list = newsService.getNews();
        return new ResponseEntity<>(list.toArray(new News[list.size()]), HttpStatus.OK);
    }

    @RequestMapping(path = "/topproducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TopProduct[]> getTopProducts(String timeType, String productType) {
        if(timeType != null && productType != null) {
            List<TopProduct> list = topProductsService
                    .getTopProductsByTimeTypeAndProductType(TimeType.getEnum(timeType), ProductType.getEnum(productType));
            return new ResponseEntity<>(list.toArray(new TopProduct[list.size()]), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new TopProduct[0], HttpStatus.OK);
        }
    }

    //stupid RiskTypeToProductType for association button type and product type by risk....
    private static BiMap<String, RiskType> RiskTypeToProductType = ImmutableBiMap.<String, RiskType>builder().
            put("100% защита капитала без гарантированной доходности", RiskType.High).
            put("С участием (ограниченный риск)", RiskType.Medium).
            put("100% защита капитала плюс гарантированная доходность", RiskType.Low).build();

    @RequestMapping(path = "/allproducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object[]> getAllProducts() {
        List<Product> list = (List<Product>) dbService.getResultList(Product.class);
        setRiskType(list);
        return new ResponseEntity<>(list.toArray(), HttpStatus.OK);
    }


    @RequestMapping(path = "/productsbytype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object[]>getProductsByType(@RequestParam("types")String[] types) {
        if (types == null) {
            return getAllProducts();
        } else {
            List<String> productTypes = Lists.newArrayList();
            for(String type : types) {
                RiskType riskType = RiskType.getRiskType(type);
                if(riskType != null) {
                    productTypes.add(RiskTypeToProductType.inverse().get(riskType));
                } else {
                    logger.error("Unknown risk type: " + type);
                }
            }
            List<Product> result = (List<Product>) dbService.getProductsByType(productTypes);
            setRiskType(result);
        /*List<Product> result = Lists.newArrayList();
        List<com.structuredproducts.persistence.entities.instrument.Product> list = (List<Product>) dbService.getResultList(Product.class);
        for(Product product : list) {
            for(String type : types) {
                if(product.getProductType().getName().equals(RiskTypeToProductType.get(type))) {
                    result.add(product);
                    break;
                }
            }
        }*/
            return new ResponseEntity<>(result.toArray(), HttpStatus.OK);
        }
    }

    private static void setRiskType(List<Product> products) {
        products.parallelStream().forEach(a -> a.setRiskType(RiskTypeToProductType.get(a.getProductType().getName())));
    }

    @RequestMapping(path = "/investideas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<InvestIdea[]> getInvestIdeas(Boolean showOnMainPage) {
        List<InvestIdea> list;
        if(showOnMainPage) {
            list = investIdeasService.getInvestIdeasForMainPage();
        } else {
            list = investIdeasService.getInvestIdeas();
        }
        return new ResponseEntity<>(list.toArray(new InvestIdea[list.size()]), HttpStatus.OK);
    }

    @RequestMapping(path = "/investidea", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<InvestIdea> getInvestIdeaById(long id) {
        List<InvestIdea> list = investIdeasService.getInvestIdeas();
        InvestIdea result = list.stream().filter(v -> v.getId() == id).findFirst().get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
