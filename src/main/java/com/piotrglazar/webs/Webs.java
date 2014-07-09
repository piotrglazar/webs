package com.piotrglazar.webs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@ComponentScan
@EnableAsync
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
public class Webs {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Webs.class, args);
    }
}
