package com.piotrglazar.webs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@Configuration
@Profile("default")
public class MailConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("#{mailProperties['webs.mail.host']}")
    private String mailHost;

    @Value("#{mailProperties['webs.mail.username']}")
    private String mailUsername;

    @Value("#{mailProperties['webs.mail.password']}")
    private String mailPassword;

    @Value("#{mailProperties['webs.mail.port']?:-1}")
    private int mailPort;

    @Bean
    public PropertiesFactoryBean mailProperties() {
        final String propertiesFileName = "mail.properties";
        final ClassPathResource mailPropertyFileLocation = new ClassPathResource(propertiesFileName);

        LOG.info("Loading properties from {}", mailPropertyFileLocation);

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setIgnoreResourceNotFound(true);
        propertiesFactoryBean.setLocation(mailPropertyFileLocation);

        return propertiesFactoryBean;
    }

    @Bean
    public MailSender mailSender() {
        if (mailPort == -1) {
            LOG.info("Using fake mail sender");
            return fakeMailSender();
        } else {
            LOG.info("Using spring mail sender");
            return springMailSender();
        }
    }

    private MailSender fakeMailSender() {
        return new MailSender() {
            @Override
            public void send(final SimpleMailMessage simpleMessage) throws MailException {

            }

            @Override
            public void send(final SimpleMailMessage[] simpleMessages) throws MailException {

            }
        };
    }

    private JavaMailSenderImpl springMailSender() {
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(mailPort);
        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPassword(mailPassword);
        final Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", Boolean.toString(true));
        properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(true));
        properties.setProperty("mail.debug", Boolean.toString(true));
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

    @Bean(name = "sampleMailMessage")
    public SimpleMailMessage sampleMailMessage() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("piotr.glazar@gmail.com");
        message.setFrom(mailUsername);
        message.setSubject("yay - it works!");
        return message;
    }

    @Bean(name = "moneyTransferMessage")
    public SimpleMailMessage moneyTransferMessage() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setSubject("Money transfer");
        message.setText("Money transfer details");
        message.setTo("piotr.glazar@gmail.com");
        return message;
    }
}
