package com.structuredproducts.rest;

import com.structuredproducts.sevices.NewsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Vlad on 23.11.2015.
 */
@Controller
@RequestMapping("/v1/data")
public class DataController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private NewsService newsService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Data> getData() {
        logger.trace("Get data");
        logger.error("Get data");
        return new ResponseEntity<>(new Data("test"), HttpStatus.OK);
    }

    @RequestMapping(path = "/news",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<News[]> getNews() {
        return new ResponseEntity<>(newsService.getNews().toArray(new News[0]), HttpStatus.OK);
    }

}
