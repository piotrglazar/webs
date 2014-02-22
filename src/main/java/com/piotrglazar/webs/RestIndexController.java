package com.piotrglazar.webs;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr Glazar
 * @since 22.02.14
 */
@RestController
public class RestIndexController {

    @RequestMapping("/")
    public String index() {
        return "Hello from webs!";
    }
}
