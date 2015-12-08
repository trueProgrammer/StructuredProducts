package com.structuredproducts.rest;

import com.structuredproducts.sevices.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Vlad on 08.12.2015.
 */
@Controller
@RequestMapping("v1/service")
public class ServiceController {

    private final static Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private MailService mailService;

    @RequestMapping(path = "/mail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> sendMail() {
        mailService.sendMessage();
        return new ResponseEntity<>(new Message(), HttpStatus.OK);
    }

}
