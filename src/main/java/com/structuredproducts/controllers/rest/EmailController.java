package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Broker;
import com.structuredproducts.persistence.entities.instrument.Email;
import com.structuredproducts.persistence.entities.instrument.Product;
import com.structuredproducts.persistence.entities.instrument.SystemProperty;
import com.structuredproducts.sevices.DBService;
import com.structuredproducts.sevices.MailService;
import com.structuredproducts.sevices.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Vlad on 08.12.2015.
 */
@Controller
@RequestMapping("v1/service")
public class EmailController {

    private static final Logger log = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private DBService dbService;

    @Value( "${mail.service_mail_property}" )
    private String serviceMailProperty;

    private final static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private MailService mailService;

    private String getServiceEmail() {
        SystemProperty prop = dbService.getObjectByKey(SystemProperty.class, serviceMailProperty);
        if (prop != null) {
            return prop.getValue();
        } else {
            log.error("There is no system property {} in database", serviceMailProperty);
            return null;
        }
    }

    @RequestMapping(path = "/email",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> sendMail(@RequestBody String request) {
        logger.debug("Send main request {} ", request);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(request);
            mailService.sendQuestion((String) map.get("name"),
                    (String) map.get("midName"),
                    (String) map.get("secondName"),
                    (String) map.get("email"),
                    (String) map.get("phone"),
                    (String) map.get("text"),
                    getServiceEmail());
            return new ResponseEntity<>(new Message("Message send successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path="/productRequest",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Message> productRequest( @RequestBody String json) {
        logger.debug("Got create product request {} ", json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);

            Integer productId = (Integer) map.get("productId");
            Product product = dbService.getObjectByKey(Product.class, productId);

            String recipients = getServiceEmail() + ",";
            recipients += String.join(",", product.getBroker().getEmails().stream().map(Email::getEmail).collect(Collectors.toList()));

            mailService.sendRequest((String) map.get("firstname"),
                    (String) map.get("lastname"),
                    (String) map.get("email"),
                    (String) map.get("phone"),
                    recipients,
                    product);

            return new ResponseEntity<>(new Message("Message send successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path="/createProductRequest",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Message> sendCreateProductRequest( @RequestBody String json) {
        logger.debug("Got create product request {} ", json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Map<String, Object> controls = (Map<String, Object>) map.get("controls");

            String recipients = getServiceEmail() + ",";

            String brokerName = getOptionalParameter("brokerBlock", controls);
            Broker broker = dbService.getByName(brokerName, Broker.class);
            if (broker != null) {
                recipients += String.join(",", broker.getEmails().stream().map(Email::getEmail).collect(Collectors.toList()));
            }

            mailService.sendCreateRequest(
                    (String) map.get("firstname"),
                    (String) map.get("lastname"),
                    (String) map.get("email"),
                    (String) map.get("phone"),
                    recipients,
                    (String)((Map)controls.get("profitBlock")).get("value"),
                    (String)((Map)controls.get("timeBlock")).get("value"),
                    (String)((Map)controls.get("sumBlock")).get("value"),
                    (String)((Map)controls.get("riskBlock")).get("value"),

                    brokerName,
                    getOptionalParameter("paymentsPeriodBlock", controls),
                    getOptionalParameter("baseActiveTypeBlock", controls),
                    getOptionalParameter("strategyBlock", controls)
            );
            return new ResponseEntity<>(new Message("Message send successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static String getOptionalParameter(final String param, final Map<String, Object> params) {
        Map map = (Map)params.get(param);
        if (map != null) {
            return (String) map.get("value");
        } else {
            return StringUtils.EMPTY;
        }
    }
}
