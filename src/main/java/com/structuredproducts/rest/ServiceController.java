package com.structuredproducts.rest;

import com.structuredproducts.sevices.MailService;
import com.structuredproducts.sevices.ServiceException;
import com.structuredproducts.util.ServiceUtils;
import org.apache.log4j.Logger;
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
public class ServiceController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private MailService mailService;

    @RequestMapping(path = "/email",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> sendMail(@RequestBody String request) {
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(request);
            mailService.sendMessage((String) map.get("name"), (String) map.get("email"), (String) map.get("text"));
            return new ResponseEntity<>(new Message("Message new Message()"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
