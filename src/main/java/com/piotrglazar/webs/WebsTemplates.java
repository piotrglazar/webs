package com.piotrglazar.webs;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.math.BigDecimal;

@Component
public class WebsTemplates {

    private final VelocityEngine velocityEngine;

    @Autowired
    public WebsTemplates(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public String mailMessage(final String username, final Long fromAccountId, final Long toAccountId, final BigDecimal amount) {
        final VelocityContext context = new VelocityContext();
        context.put("user", username);
        context.put("fromAccount", fromAccountId);
        context.put("amount", amount);
        context.put("toAccount", toAccountId);
        final Template template = velocityEngine.getTemplate("moneyTransfer.vm");
        final StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
