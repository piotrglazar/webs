package com.piotrglazar.webs.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestIndexController {

    @RequestMapping("/hello")
    public String index() {
        return "Hello from webs!";
    }
}
