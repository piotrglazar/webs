package com.piotrglazar.webs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import java.lang.invoke.MethodHandles;

@Configuration
public class UtilityConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public PasswordEncoder websPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public VelocityEngineFactoryBean velocityEngine() {
        final VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
        LOG.info("Using template from {}", "velocity");
        bean.setResourceLoaderPath("classpath:velocity");
        return bean;
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
