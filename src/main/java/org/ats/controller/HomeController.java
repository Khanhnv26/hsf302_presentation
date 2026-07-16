package org.ats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // IoC
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        // Return view
        return "views/public/home";
    }

    @GetMapping("/contact")
    public String contact() {
        return "views/public/contact";
    }

}
