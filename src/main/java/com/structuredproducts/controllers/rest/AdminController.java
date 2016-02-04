package com.structuredproducts.controllers.rest;

import com.google.common.collect.ImmutableMap;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.sevices.DBService;
import com.structuredproducts.sevices.ServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/admin")
public class AdminController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private DBService dbService;

    private static final Map<String, Class<?>> ENTITY_TYPES = ImmutableMap.<String, Class<?>>builder().
            put("productType", ProductType.class).
            put("term", Term.class).
            put("investment", Investment.class).
            put("issuer", Issuer.class).
            put("return", Return.class).
            put("strategy", Strategy.class).
            put("legalType", LegalType.class).
            put("payoff", PayOff.class).
            put("underlayingType", UnderlayingType.class).
            build();


    @RequestMapping(path = "/instrumentType/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> updateValues(@RequestParam("entityType")String entityType, @RequestBody String entity) {
        try {
            List<?> list = ServiceUtils.getObjects(ENTITY_TYPES.get(entityType), entity);
            dbService.save(list);
        } catch (IOException e) {
            logger.error("Error while convert from json to object: ["+entityType+"], ["+entity+"]", e);
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/instrumentType/delete",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> deleteValues(@RequestParam("entityType")String entityType, @RequestBody String entity) {
        try {
            List<?> list = ServiceUtils.getObjects(ENTITY_TYPES.get(entityType), entity);
            dbService.remove(list);
        } catch (IOException e) {
            logger.error("Error while convert from json to object: ["+entityType+"], ["+entity+"]", e);
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/instrumentType", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object[]> getValues(@RequestParam("entityType")String entityType) {
        Class<?> clazz = ENTITY_TYPES.get(entityType);
        List<?> list = dbService.getResultList(clazz);
        return new ResponseEntity<>(list.toArray(), HttpStatus.OK);
    }

}
