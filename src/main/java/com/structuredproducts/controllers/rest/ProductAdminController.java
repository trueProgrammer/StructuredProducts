package com.structuredproducts.controllers.rest;

import com.google.common.base.Joiner;
import com.structuredproducts.controllers.data.Message;
import com.structuredproducts.persistence.entities.instrument.Nameble;
import com.structuredproducts.persistence.entities.instrument.Product;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(AbstractAdminController.rootUrl)
public class ProductAdminController extends AbstractAdminController{
    private final static Logger logger = LoggerFactory.getLogger(ProductAdminController.class);

    @RequestMapping(path = "/instrumentType/update",
                           method = RequestMethod.POST,
                           produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                           consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Message> updateValues(@RequestParam("entityType")String entityType, @RequestBody String entity) {
        try {
            List<?> list = ServiceUtils.getObjects(ENTITY_TYPES.get(entityType), entity);
            if (dbService.save(list)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Message("Ошибка при сохранении продукта. Возможно, вы заполнили не все поля."), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            logger.error("Error while convert from json to object. Entity type:{} json: {}",entityType,entity);
            logger.error("", e);
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/products/csv",
                           method = RequestMethod.POST,
                           consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Message> uploadCsv(@RequestParam(value="broker") String broker, @RequestBody MultipartFile file) {

        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream(), "UTF-8");
            if ((char)reader.read() != '\uFEFF') {//Skip BOM symbol
                reader = new InputStreamReader(file.getInputStream(), "UTF-8");
            }
            converter.convertToDb(reader, broker);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            logger.error("Error while convert from json to object. Entity type:{} json: {}",entityType,entity);
            logger.error("", e);
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
        if(Product.class.isAssignableFrom(clazz)) {
            setUnderlayings((List<Product>) list);
        }
        return new ResponseEntity<>(list.toArray(), HttpStatus.OK);
    }

    private static final Joiner joiner = Joiner.on(", ");

    private static void setUnderlayings(List<Product> products) {
        products.parallelStream().forEach(
                a -> a.setUnderlayings(joiner.join(a.getUnderlaying().parallelStream().map(b -> b.getName()).collect(Collectors.toList())))
        );
    }
}
