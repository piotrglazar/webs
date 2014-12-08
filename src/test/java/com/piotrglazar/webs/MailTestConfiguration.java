package com.piotrglazar.webs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class MailTestConfiguration {

    @Bean
    public MailSender testMailSender() {
        return mock(MailSender.class);
    }

    @Bean(name = "websMessageTemplate")
    public SimpleMailMessage testMailMessage() {
        return mock(SimpleMailMessage.class);
    }
}
