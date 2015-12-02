package com.structuredproducts.mvc;

import com.structuredproducts.SpitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by Vlad on 22.11.2015.
 */
@Controller
public class HomeController {

    public static final int DEFAULT_SPITTER_PER_PAGE = 25;

    @Autowired
    private SpitterService spitterService;

    @RequestMapping({"/","home"})
    public String showHomePage(Map<String, Object> model) {
        model.put("spitters", 10);
        return "index";
    }
}
