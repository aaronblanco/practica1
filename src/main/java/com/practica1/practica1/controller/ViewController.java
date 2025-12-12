package com.practica1.practica1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        return "index";     // templates/index.html
    }

}
