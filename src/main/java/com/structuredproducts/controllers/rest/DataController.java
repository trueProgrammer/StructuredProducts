package com.structuredproducts.controllers.rest;

import com.google.common.collect.Lists;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.controllers.data.ProductType;
import com.structuredproducts.controllers.data.TimeType;
import com.structuredproducts.controllers.data.Tuple;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.sevices.DBService;
import com.structuredproducts.sevices.YahooCurrencyPriceService;
import com.structuredproducts.sevices.YahooUnderlayingPriceService;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @RequestMapping(path="/brokerGet",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Object[]> getBrokers() {
        List<?> list = dbService.getResultList(Broker.class);
        return new ResponseEntity<>(list.toArray(), HttpStatus.OK);
    }

    @RequestMapping(path = "/topproducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Product[]> getTopProducts(@RequestParam(name="time") String timeType, @RequestParam(name="type")String productType) {
        logger.debug("Time {}, product type {}", timeType, productType);
        List<Product> list = ControllerUtils.getProducts(dbService.getTopProductsByTimeTypeAndProductType(timeType, productType));
        return new ResponseEntity<>(list.toArray(new Product[list.size()]), HttpStatus.OK);
    }



    @RequestMapping(path = "/allproducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object[]> getAllProducts() {
        return new ResponseEntity<>(getProducts().toArray(), HttpStatus.OK);
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
                    productTypes.add(ControllerUtils.RiskTypeToProductType.inverse().get(riskType));
                } else {
                    logger.error("Unknown risk type: {}", type);
                }
            }
            return new ResponseEntity<>( getProductsByType(productTypes).toArray(), HttpStatus.OK);
        }
    }

    private List<Product> getProductsByType(List<String> productTypes) {
        return ControllerUtils.getProducts((List<Product>) dbService.getProductsByType(productTypes));
    }

    private List<Product> getProducts() {
        return ControllerUtils.getProducts((List<Product>) dbService.getResultList(Product.class));
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
        Optional<Product> productOpt = (ControllerUtils.getProducts((List<Product>) dbService.getResultList(Product.class))).stream().filter(p -> p.getId().equals(id)).findAny();
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

    @RequestMapping(path="/historicalquotes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Object> getUnderlayingHistoricalQuotes(@RequestParam("id")Integer id) {
        Optional<Product> productOpt = (ControllerUtils.getProducts((List<Product>) dbService.getResultList(Product.class))).stream().filter(p -> p.getId().equals(id)).findAny();
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            List<HistoricalHolder> result = Lists.newArrayList();
            product.getUnderlayingList().parallelStream().forEach(
                    v -> {
                        try {
                            HistoricalHolder holder = new HistoricalHolder();
                            Map<String, String> historical = YahooUnderlayingPriceService.cache.get(v.getOfficialName());
                            if (historical.isEmpty()) {
                                historical = YahooCurrencyPriceService.cache.get(v.getOfficialName());
                            }
                            holder.name = v.getName();
                            for(Map.Entry<String, String> entry : historical.entrySet()) {
                                holder.labels.add(entry.getKey());
                                holder.dataset.add(entry.getValue());
                            }
                            Collections.reverse(holder.labels);
                            Collections.reverse(holder.dataset);
                            result.add(holder);
                            //result.put(v.getName(), YahooUnderlayingPriceService.getYearHistoricalQuotes(v.getOfficialName()));
                        } catch (Exception e) {
                            logger.error("Error while get historical quotes for underlaying: " + v.getName(), e);
                        }
                    }
            );
            return new ResponseEntity<>(result.toArray(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    static final class HistoricalHolder {
        public String name;
        public List<String> labels = Lists.newArrayList();
        public List<String> dataset = Lists.newArrayList();
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
