package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.util.OperationLogging;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class BusinessMailSender {

    private final MailSender mailSender;

    private final SimpleMailMessage websMessageTemplate;

    private final WebsTemplates websTemplates;

    @Autowired
    public BusinessMailSender(MailSender mailSender, WebsTemplates websTemplates,
                              @Qualifier("websMessageTemplate") SimpleMailMessage websMessageTemplate) {
        this.mailSender = mailSender;
        this.websTemplates = websTemplates;
        this.websMessageTemplate = websMessageTemplate;
    }

    @Async
    @OperationLogging(operation = "moneyTransferMessage")
    public void sendMoneyTransferMessage(final MoneyTransferParams moneyTransferParams) {
        final SimpleMailMessage message = new SimpleMailMessage(websMessageTemplate);
        final String messageText = moneyTransferMailMessageText(moneyTransferParams);

        message.setSubject("Money transfer");
        message.setText(messageText);
        message.setTo(moneyTransferParams.getEmail());

        mailSender.send(message);
    }

    @Async
    @OperationLogging(operation = "passwordResetMessage")
    public void sendPasswordResetMessage(final WebsUser websUser, final URL passwordResetUrl) {
        final SimpleMailMessage message = new SimpleMailMessage(websMessageTemplate);
        final String messageText = passwordResetMailMessageText(websUser, passwordResetUrl);

        message.setSubject("Password reset");
        message.setText(messageText);
        message.setTo(websUser.getEmail());

        mailSender.send(message);
    }

    private String passwordResetMailMessageText(final WebsUser websUser, final URL passwordResetUrl) {
        return websTemplates.passwordResetMailMessage(websUser.getUsername(), passwordResetUrl);
    }

    private String moneyTransferMailMessageText(final MoneyTransferParams moneyTransferParams) {
        return websTemplates.moneyTransferMailMessage(moneyTransferParams.getUsername(), moneyTransferParams.getFromAccount(),
                moneyTransferParams.getToAccount(), moneyTransferParams.getAmount(), moneyTransferParams.getReceivingUsername(),
                moneyTransferParams.getCurrency());
    }
}
