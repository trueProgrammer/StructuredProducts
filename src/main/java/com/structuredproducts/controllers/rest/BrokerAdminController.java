package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Broker;
import com.structuredproducts.persistence.entities.instrument.Email;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(AbstractAdminController.rootUrl)
public class BrokerAdminController extends AbstractAdminController{
    Logger logger = LoggerFactory.getLogger(BrokerAdminController.class);

    @RequestMapping(path="/brokerAdd",
                           method = RequestMethod.POST,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> addBroker(@RequestBody String json) {

        logger.debug("Got json {}", json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            String name = (String) map.get("name");
            String img = (String) map.get("img");

            Broker broker = new Broker();
            broker.setId(id);
            broker.setName(name);
            broker.setLogo(img);

            Broker savedBroker = (Broker)dbService.save(broker);
            List<Email> emails = ((List<LinkedHashMap>) map.get("emails"))
                    .stream()
                    .map(m -> new Email((Integer)m.get("id"), (String)m.get("email"), savedBroker))
                    .collect(Collectors.toList());
            savedBroker.setEmails(emails);
            dbService.save(emails);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path="/brokerRemove",
                           method = RequestMethod.POST,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> removeBroker(@RequestBody String json) {
        logger.debug("Got json {}", json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            Broker broker = new Broker();
            broker.setId(id);
            dbService.removeObj(broker);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
