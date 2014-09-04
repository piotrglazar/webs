package com.piotrglazar.webs.business;

import com.piotrglazar.webs.WebsTemplates;
import com.piotrglazar.webs.util.OperationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BusinessMailSender {

    private final MailSender mailSender;

    private final SimpleMailMessage moneyTransferMessage;

    private final WebsTemplates websTemplates;

    @Autowired
    public BusinessMailSender(final MailSender mailSender, final WebsTemplates websTemplates,
                              @Qualifier("moneyTransferMessage") final SimpleMailMessage moneyTransferMessage) {
        this.mailSender = mailSender;
        this.websTemplates = websTemplates;
        this.moneyTransferMessage = moneyTransferMessage;
    }

    @Async
    @OperationLogging(operation = "moneyTransferMessage")
    public void sendMoneyTransferMessage(final MoneyTransferParams moneyTransferParams) {
        final SimpleMailMessage message = new SimpleMailMessage(moneyTransferMessage);
        final String messageText = mailMessageText(moneyTransferParams);

        message.setText(messageText);
        message.setTo(moneyTransferParams.getEmail());

        mailSender.send(message);
    }

    private String mailMessageText(final MoneyTransferParams moneyTransferParams) {
        return websTemplates.mailMessage(moneyTransferParams.getUsername(), moneyTransferParams.getFromAccount(),
                moneyTransferParams.getToAccount(), moneyTransferParams.getAmount(), moneyTransferParams.getReceivingUsername());
    }
}
