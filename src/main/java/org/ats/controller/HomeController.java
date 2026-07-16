package org.ats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // IoC
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {

        // Logic OK: service-> repository


        // Return view
        return "views/public/home"; // prefix + "index" + suffix -> /WEB-INF/index.jsp
    }

}
