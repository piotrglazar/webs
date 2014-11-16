package com.piotrglazar.webs.business;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.util.templates.WebsTemplates;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class BusinessMailSenderTest {

    @Mock
    private WebsTemplates websTemplates;

    @Mock
    private MailSender mailSender;

    @Mock
    private SimpleMailMessage message;

    @InjectMocks
    private BusinessMailSender businessMailSender;

    @Test
    public void shouldSendMessage() {
        // given
        given(websTemplates.mailMessage("user", 1234L, 4321L, BigDecimal.valueOf(5555), "user2", Currency.GBP)).willReturn("mailmessage");
        final MoneyTransferParams params = new MoneyTransferParamsBuilder()
                .username("user")
                .email("email")
                .fromAccount(1234L)
                .toAccount(4321L)
                .amount(BigDecimal.valueOf(5555))
                .receivingUserId(1L)
                .receivingUsername("user2")
                .currency(Currency.GBP)
                .build();

        // when
        businessMailSender.sendMoneyTransferMessage(params);

        // then
        final ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        final String messageText = captor.getValue().getText();
        assertThat(messageText).contains("mailmessage");
    }
}
