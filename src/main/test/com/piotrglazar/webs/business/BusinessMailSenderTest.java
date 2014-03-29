package com.piotrglazar.webs.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class BusinessMailSenderTest {

    @Mock
    private MailSender mailSender;

    @Mock
    private SimpleMailMessage message;

    @InjectMocks
    private BusinessMailSender businessMailSender;

    @Test
    public void shouldSendMessage() {
        // given
        final MoneyTransferParams params = new MoneyTransferParams("user", "email", 1234L, 4321L, BigDecimal.valueOf(5555));

        // when
        businessMailSender.sendMoneyTransferMessage(params);

        // then
        final ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        final String messageText = captor.getValue().getText();
        assertThat(messageText).contains("user", "1234", "4321", "5555");
    }
}
