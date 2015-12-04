package com.structuredproducts.rest;

import org.apache.log4j.Logger;
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
public class RestController {

    private final static Logger logger = Logger.getLogger(RestController.class);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Data> getData() {
        logger.trace("Get data");
        logger.error("Get data");
        return new ResponseEntity<>(new Data("test"), HttpStatus.OK);
    }

}
