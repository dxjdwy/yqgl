package com.casic.yqgl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {
    @RequestMapping(value = {"/","index"})
    public String index(Model model){
        return "index";
    }
    @RequestMapping("/about")
    public String about(Model model){
        return "about";
    }
}
