package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.SystemProperty;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(AbstractAdminController.rootUrl)
public class SystemPropertiesAdminController extends AbstractAdminController {
    @Autowired
    private MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(ProductParamsAdminController.class);

    @RequestMapping(path = "systemproperties/get",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<List<SystemProperty>> getSystemProperties() {
        return new ResponseEntity<>((List<SystemProperty>) dbService.getResultList(SystemProperty.class), HttpStatus.OK);
    }

    @RequestMapping(path = "systemproperties/modify",
            method = RequestMethod.POST
    )
    public ResponseEntity<Message> modifySystemProperty(@RequestBody String json) throws IOException {
        logger.debug("Got update system property request {} ", json);

        Map<String, Object> map = ServiceUtils.getObjectMapping(json);
        String key = (String) map.get("key");
        String value = (String) map.get("value");

        SystemProperty systemProperty = new SystemProperty(key, value);
        dbService.save(systemProperty);
        return new ResponseEntity<>(new Message(), HttpStatus.OK);
    }
}
