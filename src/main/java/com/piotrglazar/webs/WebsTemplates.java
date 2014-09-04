package com.piotrglazar.webs;

import com.piotrglazar.webs.business.BloombergNewsBody;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebsTemplates {

    private static final String PREFIX = "velocity/";

    private final VelocityEngine velocityEngine;

    @Autowired
    public WebsTemplates(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public String mailMessage(final String username, final Long fromAccountId, final Long toAccountId, final BigDecimal amount,
                              final String receivingUsername) {
        final VelocityContext context = new VelocityContext();
        context.put("user", username);
        context.put("fromAccount", fromAccountId);
        context.put("amount", amount);
        context.put("toAccount", toAccountId);
        context.put("receivingUser", receivingUsername);
        final Template template = velocityEngine.getTemplate(PREFIX + "moneyTransfer.vm");
        final StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    public String bloombergNewsBody(final List<BloombergNewsBody> bloombergNewBodies) {
        final VelocityContext context = new VelocityContext();
        context.put("tickers", bloombergNewBodies.stream().map(BloombergNewsBody::asMap).collect(Collectors.toList()));
        final Template template = velocityEngine.getTemplate(PREFIX + "bloombergNewsBody.vm");
        final StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
