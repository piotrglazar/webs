package com.piotrglazar.webs.util.templates;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.BloombergNewsBody;
import com.piotrglazar.webs.dto.ExchangeRateDto;
import com.piotrglazar.webs.util.MoreCollectors;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

@Component
public class WebsTemplates {

    private static final String PREFIX = "velocity/";

    private final VelocityEngine velocityEngine;

    @Autowired
    public WebsTemplates(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public String moneyTransferMailMessage(final String username, final Long fromAccountId, final Long toAccountId, final BigDecimal amount,
                                           final String receivingUsername, final Currency currency) {
        final VelocityContext context = new VelocityContext();
        context.put("user", username);
        context.put("fromAccount", fromAccountId);
        context.put("amount", String.format("%s %s", amount, currency));
        context.put("toAccount", toAccountId);
        context.put("receivingUser", receivingUsername);
        return fillInTemplate(context, "moneyTransfer.vm");
    }

    public String bloombergNewsBody(final List<BloombergNewsBody> bloombergNewBodies) {
        final VelocityContext context = new VelocityContext();
        context.put("tickers", bloombergNewBodies.stream().map(BloombergNewsBody::asMap).collect(MoreCollectors.toImmutableList()));
        return fillInTemplate(context, "bloombergNewsBody.vm");
    }

    public String exchangeRatesNewsBody(final ExchangeRateDto exchangeRate) {
        final VelocityContext context = new VelocityContext();
        context.put("exchangeRate", exchangeRate);
        return fillInTemplate(context, "exchangeRatesNewsBody.vm");
    }

    public String passwordResetMailMessage(final String username, final URL passwordResetUrl) {
        final VelocityContext context = new VelocityContext();
        context.put("username", username);
        context.put("passwordResetUrl", passwordResetUrl);
        return fillInTemplate(context, "passwordResetMessage.vm");
    }

    private String fillInTemplate(final VelocityContext context, final String templateName) {
        final Template template = velocityEngine.getTemplate(PREFIX + templateName);
        final StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
