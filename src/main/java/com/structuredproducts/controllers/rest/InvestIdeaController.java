package com.structuredproducts.controllers.rest;

import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Broker;
import com.structuredproducts.persistence.entities.instrument.InvestIdea;
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
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(AbstractAdminController.rootUrl)
public class InvestIdeaController extends AbstractAdminController{
    Logger logger = LoggerFactory.getLogger(InvestIdeaController.class);

    @RequestMapping(path = "/removeIdea", method = RequestMethod.POST,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> removeInvestIdea(@RequestBody() String json) {
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            InvestIdea idea = new InvestIdea();
            idea.setId((Integer) map.get("id"));
            dbService.removeObj(idea);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<Message>(HttpStatus.OK);
    }
    @RequestMapping(path="/investIdeaAddOrUpdate",
                           method = RequestMethod.POST,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Message> addInvestIdea(@RequestBody String json) {
        logger.debug("Got json {} for save ", json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            String title = (String) map.get("title");
            String content = (String) map.get("content");
            Integer brokerId = (Integer) map.get("broker");
            Boolean onMainPage = (Boolean) map.get("onMainPage");


            InvestIdea idea = new InvestIdea();
            idea.setId(id);
            if (brokerId != null) {
                Broker broker = new Broker();
                broker.setId(brokerId);
                idea.setBroker(broker);
            }
            idea.setTitle(title);
            idea.setContent(content);
            idea.setAddDate(new Date());
            idea.setMainPage(onMainPage);

            if (dbService.save(idea) != null) {
                return new ResponseEntity<>(new Message("При сохранении идеи произошла ошибка. Обратитесь к администратору."), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
            return new ResponseEntity<>(new Message("При сохранении идеи произошла ошибка. Обратитесь к администратору."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
