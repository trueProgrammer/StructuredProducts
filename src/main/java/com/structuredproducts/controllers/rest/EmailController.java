package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.sevices.MailService;
import com.structuredproducts.sevices.ServiceUtils;
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

import java.util.Map;

/**
 * Created by Vlad on 08.12.2015.
 */
@Controller
@RequestMapping("v1/service")
public class EmailController {

    private final static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private MailService mailService;

    @RequestMapping(path = "/email",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> sendMail(@RequestBody String request) {
        logger.debug("Send main request {} ", request);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(request);
            mailService.sendMessage((String) map.get("name"),
                    (String) map.get("midName"),
                    (String) map.get("secondName"),
                    (String) map.get("email"),
                    (String) map.get("phone"),
                    (String) map.get("text"),
                    ""//TODO add admin mail
            );
            return new ResponseEntity<>(new Message("Message new Message()"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
