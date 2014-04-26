package com.piotrglazar.webs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@ComponentScan
@EnableAsync
@EnableAutoConfiguration
@EnableTransactionManagement
public class ServiceRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ServiceRunner.class, args);
    }
}
