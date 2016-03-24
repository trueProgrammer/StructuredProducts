package com.structuredproducts.controllers.rest;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.controllers.data.ProductType;
import com.structuredproducts.controllers.data.TimeType;
import com.structuredproducts.controllers.data.Tuple;
import com.structuredproducts.persistence.entities.instrument.InvestIdea;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.persistence.entities.instrument.ProductParam;
import com.structuredproducts.persistence.entities.instrument.RiskType;
import com.structuredproducts.sevices.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Created by Vlad on 23.11.2015.
 */
@Controller
@RequestMapping("/v1/data")
public class DataController {

    private final static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private DBService dbService;

    @RequestMapping(path = "/timetypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Tuple[]> getTimeTypes() {
        TimeType[] timeTypes = TimeType.values();
        Tuple[] values = new Tuple[timeTypes.length];
        int i = 0;
        for(TimeType timeType : timeTypes) {
            values[i++] = new Tuple(TimeType.getName(timeType), timeType.getName());
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

    @RequestMapping(path = "/topproducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Product[]> getTopProducts(@RequestParam(name="time") String timeType, @RequestParam(name="type")String productType) {
        logger.debug("Time {}, product type {}", timeType, productType);
        List<Product> list = dbService.getTopProductsByTimeTypeAndProductType(timeType, productType);
        list.forEach(product -> product.getInvestment().setName());
        return new ResponseEntity<>(list.toArray(new Product[list.size()]), HttpStatus.OK);
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
                    logger.error("Unknown risk type: {}", type);
                }
            }
            List<Product> result = (List<Product>) dbService.getProductsByType(productTypes);
            setRiskType(result);
            return new ResponseEntity<>(result.toArray(), HttpStatus.OK);
        }
    }

    private static void setRiskType(List<Product> products) {
        products.parallelStream().forEach(a -> a.setRiskType(RiskTypeToProductType.get(a.getProductType().getName())));
    }

    @RequestMapping(path = "/investideas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<InvestIdea[]> getInvestIdeas(Boolean showOnMainPage) {
        List<InvestIdea> list = (List<InvestIdea>) dbService.getResultList(InvestIdea.class);
        if (list != null) {
            list.forEach(idea -> idea.setPreview());
        }
        return new ResponseEntity<>(list.toArray(new InvestIdea[list.size()]), HttpStatus.OK);
    }

    @RequestMapping(path = "/investidea", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<InvestIdea> getInvestIdeaById(int id) {
        return new ResponseEntity<>(dbService.getInvestIdeaById(id), HttpStatus.OK);
    }

    @RequestMapping(path = "/productparams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ProductParam[]> getProductsWithParams() {
        List<Product> products = (List<Product>) dbService.getResultList(Product.class);
        List<ProductParam> params = (List<ProductParam>) dbService.getResultList(ProductParam.class);
        products.forEach(p -> {
            if (params.stream().noneMatch(param -> param.getProduct().getId().equals(p.getId()))) {
               params.add(new ProductParam(p));
            }
        });
        return new ResponseEntity<>((ProductParam[]) params.toArray(new ProductParam[params.size()]), HttpStatus.OK);
    }

    @RequestMapping(path="/productwithparams",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<ProductParam> getProductWithParams(@RequestParam("id")Integer id) {
        Optional<Product> productOpt = ((List<Product>) dbService.getResultList(Product.class)).stream().filter(p -> p.getId().equals(id)).findAny();
        List<ProductParam> params = (List<ProductParam>) dbService.getResultList(ProductParam.class);
        if (!productOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Product product = productOpt.get();
        Optional<ProductParam> paramOpt = params.stream().filter(par -> par.getProduct().getId().equals(product.getId())).findAny();
        if (paramOpt.isPresent()) {
            return new ResponseEntity<>(paramOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ProductParam(product), HttpStatus.OK);
    }

    @RequestMapping(path="/createProductRequest",
                           method = RequestMethod.POST,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Message> sendCreateProductRequest( @RequestBody String productRequest) {
        logger.debug("Got create product request {} ", productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
