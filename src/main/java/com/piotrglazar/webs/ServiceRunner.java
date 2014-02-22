package com.piotrglazar.webs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Piotr Glazar
 * @since 02.02.14
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ServiceRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ServiceRunner.class, args);
    }
}
