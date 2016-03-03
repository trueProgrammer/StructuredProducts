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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
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
            converter.convertToDb(reader);
        } catch (IOException e) {
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "instrument/download",
        method = RequestMethod.GET)
    public void getCsv(HttpServletResponse response) {
        try {
            String productsCsv = converter.convertToCsv();

            response.getOutputStream().write(productsCsv.getBytes(Charset.forName("UTF-8")));
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @RequestMapping(path="/brokerAdd",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> addBroker(@RequestBody String json) {
        logger.debug("Got json " + json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            String name = (String) map.get("name");
            String img = (String) map.get("img");

            Broker broker = new Broker();
            broker.setId(id);
            broker.setName(name);
            broker.setLogo(img);

            dbService.save(broker);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(path="/brokerRemove",
                           method = RequestMethod.POST,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> removeBroker(@RequestBody String json) {
        logger.debug("Got json " + json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            Broker broker = new Broker();
            broker.setId(id);
            dbService.removeObj(broker);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(path="/brokerGet",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Object[]> getBrokers() {
        List<?> list = dbService.getResultList(Broker.class);
        return new ResponseEntity<>(list.toArray(), HttpStatus.OK);
    }

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
            logger.error("can't handle json " + json);
        }
        return new ResponseEntity<Message>(HttpStatus.OK);
    }
    @RequestMapping(path="/investIdeaAddOrUpdate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Message> addInvestIdea(@RequestBody String json) {
        logger.debug("Got json for save " + json);
        try {
            Map<String, Object> map = ServiceUtils.getObjectMapping(json);
            Integer id = (Integer) map.get("id");
            String title = (String) map.get("title");
            String content = (String) map.get("content");
            Integer brokerId = (Integer) map.get("broker");

            Broker broker = new Broker();
            broker.setId(brokerId);

            InvestIdea idea = new InvestIdea();
            idea.setId(id);
            idea.setBroker(broker);
            idea.setTitle(title);
            idea.setContent(content);
            idea.setAddDate(new Date());

            dbService.save(idea);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
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

    @RequestMapping(path="/getTopProducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object[]> getTopProducts(@RequestParam(name="time") String time) {
        List<Product> products = (List<Product>) dbService.getResultList(Product.class);
        List<TopProduct> topProducts = (List<TopProduct>) dbService.getResultList(TopProduct.class);
        products.forEach(product -> {
            if (topProducts.stream().anyMatch(topProduct -> topProduct.getProduct().getId().equals(product.getId()) && topProduct.getTime().equals(time))) {
                product.setIsTop(true);
            } else {
                product.setIsTop(false);
            }
        });

        return new ResponseEntity<Object[]>(products.toArray(), HttpStatus.OK);
    }

    @RequestMapping(path="/addToTop",
                           method = RequestMethod.POST,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> addToTopProducts(@RequestBody String json) {
        logger.debug("Got json " + json);

        try {
            List<TopProduct> products = extractTopProductsFromJson(json);
            dbService.save(products);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path="/removeFromTop",
                           method = RequestMethod.POST,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> removeFromTopProducts(@RequestBody String json) {
        logger.debug("Got json " + json);
        try {
            List<TopProduct> products = extractTopProductsFromJson(json);
            dbService.removeTopProductByProduct(products);
        } catch (IOException e) {
            logger.error("can't handle json " + json, e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<TopProduct> extractTopProductsFromJson(@RequestBody String json) throws IOException {
        Map<String, Object> map = ServiceUtils.getObjectMapping(json);
        List<Integer> ids = (List<Integer>) map.get("ids");
        String time = (String) map.get("time");
        List<TopProduct> products = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            TopProduct topProduct = new TopProduct();
            Product product = new Product();
            product.setId(ids.get(i));

            topProduct.setProduct(product);
            topProduct.setTime(time);
            products.add(topProduct);
        }
        return products;
    }

}
