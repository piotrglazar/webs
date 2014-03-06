package com.piotrglazar.webs;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Piotr Glazar
 * @since 23.02.14
 */
@Component
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
    }
}
