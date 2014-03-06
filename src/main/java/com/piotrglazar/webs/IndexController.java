package com.piotrglazar.webs;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Piotr Glazar
 * @since 02.02.14
 */
@Controller
public class IndexController {

    @RequestMapping({"/index", "/"})
    public String index(final Model model) {
        model.addAttribute("message", "Welcome to the brand-new online bank!");
        return "index";
    }
}
