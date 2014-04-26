package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.config.MvcConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping({"/index", "/"})
    public String index(final Model model) {
        model.addAttribute("message", "Welcome to the brand-new online bank!");
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "index");
        return "index";
    }
}
