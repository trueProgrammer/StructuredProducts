package com.structuredproducts.controllers.rest;

import com.google.common.collect.ImmutableMap;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.sevices.ServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/v1/admin")
public class AdminController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    private static final Map<String, Class<?>> ENTITY_TYPES = ImmutableMap.<String, Class<?>>builder().
            put("productType", ProductType.class).
            put("term", Term.class).
            put("investment", Investment.class).
            put("issuer", Issuer.class).
            put("return", Return.class).
            build();


    @RequestMapping(path = "/instrumentType",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> sendMail(@RequestParam("entityType")String entityType, @RequestBody String entity) {
        try {
            ServiceUtils.getObject(ENTITY_TYPES.get(entityType), entity);
        } catch (IOException e) {
            logger.error("Error while convert from json to object: ["+entityType+"], ["+entity+"]", e);
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Message>(HttpStatus.OK);
    }

}
