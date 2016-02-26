package com.structuredproducts.controllers.rest;

import com.google.common.collect.ImmutableMap;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.*;
import com.structuredproducts.sevices.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/admin")
public class AdminController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private DBService dbService;
    @Autowired
    private ProductCsvToDbService converter;

    private static final Map<String, Class<?>> ENTITY_TYPES = ImmutableMap.<String, Class<?>>builder().
            put("productType", ProductType.class).
            put("term", Term.class).
            put("investment", Investment.class).
            put("issuer", Issuer.class).
            put("return", Return.class).
            put("strategy", Strategy.class).
            put("legalType", LegalType.class).
            put("payoff", PayOff.class).
            put("risks", Risks.class).
            put("currency", Currency.class).
            put("underlayingType", UnderlayingType.class).
            put("underlaying", Underlaying.class).
            put("product", Product.class).
            put("paymentPeriodicity", PaymentPeriodicity.class).
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

    @RequestMapping(path = "/products/csv",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Message> uploadCsv(@RequestBody MultipartFile file) {

        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream(), "UTF-8");
            converter.convert(reader);
        } catch (IOException e) {
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
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
        if(Nameble.class.isAssignableFrom(clazz)) {
            for(Object obj : list) {
                ((Nameble)obj).setName();
            }
        }
        return new ResponseEntity<>(list.toArray(), HttpStatus.OK);
    }

}
