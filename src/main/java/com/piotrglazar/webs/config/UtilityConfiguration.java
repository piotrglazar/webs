package com.piotrglazar.webs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
public class UtilityConfiguration {

    @Bean
    public PasswordEncoder websPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public VelocityEngineFactoryBean velocityEngine() {
        final VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
        bean.setResourceLoaderPath("classpath:velocity");
        return bean;
    }
}
