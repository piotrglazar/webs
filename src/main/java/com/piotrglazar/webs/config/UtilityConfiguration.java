package com.piotrglazar.webs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UtilityConfiguration {

    public static final String PASSWORD_RESET_ROOT_URL = "resetPassword";
    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    @Bean
    public PasswordEncoder websPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public DataSourcePoolMetadataProvider websMetadataProvider() {
        return WebsDataSourcePoolMetadata::new;
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
