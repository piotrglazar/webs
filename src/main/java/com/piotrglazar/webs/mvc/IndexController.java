package com.piotrglazar.webs.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping({"/index", "/"})
    public String index(final Model model) {
        model.addAttribute("message", "Welcome to the brand-new online bank!");
        return "index";
    }
}
