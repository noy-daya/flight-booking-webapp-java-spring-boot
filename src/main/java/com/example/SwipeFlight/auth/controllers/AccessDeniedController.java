package com.example.SwipeFlight.auth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied"; // Return the name of the HTML file for the error page
    }
}
