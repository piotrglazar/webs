package com.piotrglazar.webs.mvc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestIndexController {

    @RequestMapping("/hello")
    public String index() {
        return "Hello from webs!";
    }
}
