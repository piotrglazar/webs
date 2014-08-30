package com.piotrglazar.webs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.invoke.MethodHandles;

@Configuration
public class UtilityConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public PasswordEncoder websPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
