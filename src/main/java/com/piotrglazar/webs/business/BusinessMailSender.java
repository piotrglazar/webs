package com.piotrglazar.webs.business;

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

    @Autowired
    public BusinessMailSender(final MailSender mailSender,
                              @Qualifier("moneyTransferMessage") final SimpleMailMessage moneyTransferMessage) {
        this.mailSender = mailSender;
        this.moneyTransferMessage = moneyTransferMessage;
    }

    @Async
    public void sendMoneyTransferMessage(final MoneyTransferParams moneyTransferParams) {
        final SimpleMailMessage message = new SimpleMailMessage(moneyTransferMessage);
        final String messageText = mailMessageText(moneyTransferParams);

        message.setText(messageText);
        message.setTo(moneyTransferParams.getEmail());

        mailSender.send(message);
    }

    private String mailMessageText(final MoneyTransferParams moneyTransferParams) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Sending money\n");
        builder.append("Amount: ");
        builder.append(moneyTransferParams.getAmount());
        builder.append("\n");
        builder.append("From: ");
        builder.append(moneyTransferParams.getUsername());
        builder.append(" ");
        builder.append(moneyTransferParams.getFromAccount());
        builder.append("\n");
        builder.append("To: ");
        builder.append(moneyTransferParams.getToAccount());

        return builder.toString();
    }
}
