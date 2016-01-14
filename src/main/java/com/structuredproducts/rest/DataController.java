package com.structuredproducts.rest;

import com.structuredproducts.data.*;
import com.structuredproducts.sevices.InvestIdeasService;
import com.structuredproducts.sevices.NewsService;
import com.structuredproducts.sevices.TopProductsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(path = "/fileupload", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> fileupload(String file, String filename) {
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
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
