package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.business.UiNewsProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private final UiNewsProvider uiNewsProvider;

    @Autowired
    public IndexController(final UiNewsProvider uiNewsProvider) {
        this.uiNewsProvider = uiNewsProvider;
    }

    @RequestMapping({"/index", "/"})
    public String index(final Model model) {
        model.addAttribute("message", "Welcome to the brand-new online bank!");
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "index");

        model.addAttribute("websNews", uiNewsProvider.getNews());

        return "index";
    }
}
