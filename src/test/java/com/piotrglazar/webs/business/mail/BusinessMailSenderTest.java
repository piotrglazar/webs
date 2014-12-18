package com.piotrglazar.webs.business.mail;

import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.business.moneytransfer.MoneyTransferParamsBuilder;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import static com.piotrglazar.webs.business.SimpleMailMessageAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class BusinessMailSenderTest {

    @Captor
    private ArgumentCaptor<SimpleMailMessage> captor;

    @Mock
    private WebsTemplates websTemplates;

    @Mock
    private MailSender mailSender;

    @Mock
    private SimpleMailMessage message;

    @InjectMocks
    private BusinessMailSender businessMailSender;

    @Test
    public void shouldSendMoneyTransferMessage() {
        // given
        given(websTemplates.moneyTransferMailMessage("user", 1234L, 4321L, BigDecimal.valueOf(5555), "user2", Currency.GBP))
                .willReturn("mailMessage");
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
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue())
            .hasMessageText("mailMessage")
            .hasTo("email")
            .hasSubject("Money transfer");
    }

    @Test
    public void shouldSendPasswordResetMessage() throws MalformedURLException {
        // given
        final WebsUser websUser = new WebsUserBuilder().username("user").email("email").build();
        final URL passwordResetUrl = new URL("http://localhost:8080/resetPassword");
        given(websTemplates.passwordResetMailMessage("user", passwordResetUrl)).willReturn("mailMessage");

        // when
        businessMailSender.sendPasswordResetMessage(websUser, passwordResetUrl);

        // then
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue())
            .hasMessageText("mailMessage")
            .hasSubject("Password reset")
            .hasTo("email");
    }
}
