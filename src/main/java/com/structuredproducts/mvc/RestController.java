package com.structuredproducts.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Vlad on 23.11.2015.
 */
@Controller
@RequestMapping("/spitter")
public class RestController {

    @RequestMapping(value = "/spitters", method = RequestMethod.GET)
    public String listSpitter(@RequestParam("spitter") String userName, Model model) {
        return "";
    }

}
