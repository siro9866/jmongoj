package com.sil.jmongoj.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";  // /WEB-INF/jsp/index.jsp
    }
}
