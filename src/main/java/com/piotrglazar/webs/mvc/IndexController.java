package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.business.UiNewsProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    private List<String> carouselItems() {
        return Lists.newArrayList("#777:#7a7a7a/text:First slide", "#666:#6a6a6a/text:Second slide", "#555:#5a5a5a/text:Third slide");
    }
}
