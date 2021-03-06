package com.piotrglazar.webs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@EnableAsync
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableJpaRepositories
@SpringBootApplication
public class Webs {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Webs.class, args);
    }

    @Override
    public String toString() {
        return "Webs - Online bank";
    }
}
