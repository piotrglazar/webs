package com.piotrglazar.webs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.lang.invoke.MethodHandles;

@Configuration
public class BusinessConfiguration {

    public static final String INTEREST_RATE_ACCRUE_CRON_EXPRESSION = "0 0 0 * * *";
    public static final String LOAN_REPAY_CRON_EXPRESSION = "0 0 0 * * MON";

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public PropertiesFactoryBean businessProperties() {
        final String propertiesFileName = "business.properties";
        final ClassPathResource mailPropertyFileLocation = new ClassPathResource(propertiesFileName);

        LOG.info("Loading properties from {}", mailPropertyFileLocation);

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setIgnoreResourceNotFound(true);
        propertiesFactoryBean.setLocation(mailPropertyFileLocation);

        return propertiesFactoryBean;
    }
}
