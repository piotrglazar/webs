package com.piotrglazar.webs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.BloombergNewsBody;
import com.piotrglazar.webs.dto.ExchangeRateDto;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsTemplatesContextTest extends AbstractContextTest {

    @Autowired
    private WebsTemplates websTemplates;

    @Test
    public void shouldWriteMoneyTransferMessage() {
        // when
        final String template = websTemplates.moneyTransferMailMessage("user", 1L, 2L, BigDecimal.TEN, "receivingUser", Currency.GBP);

        // then
        assertThat(template)
                .isNotEmpty()
                .contains("user", "1", "2", "10", "receivingUser");
    }

    @Test
    public void shouldWriteBloombergNewsBody() {
        // given
        final BloombergNewsBody news = createBloombergNewsBodyWithNameAndPrice("name", "10012");

        // when
        final String bloombergNewsBody = websTemplates.bloombergNewsBody(Lists.newArrayList(news));

        // then
        assertThat(bloombergNewsBody)
                .isNotEmpty()
                .contains("name", "10012");
    }

    @Test
    public void shouldWritePasswordResetMessage() throws MalformedURLException {
        // given

        // when
        final String message = websTemplates.passwordResetMailMessage("piotr", new URL("http://localhost"));

        // then
        assertThat(message)
                .isNotEmpty()
                .contains("piotr", "http://localhost");
    }

    @Test
    public void shouldWriteExchangeRateNewsBody() {
        // given
        final ExchangeRateDto dto = new ExchangeRateDto("USD", ImmutableMap.of("GBP", BigDecimal.TEN, "PLN", BigDecimal.ONE));

        // when
        final String newsBody = websTemplates.exchangeRatesNewsBody(dto);

        // then
        assertThat(newsBody)
                .isNotEmpty()
                .contains("USD", "GBP", "PLN", "10", "1");
    }

    private BloombergNewsBody createBloombergNewsBodyWithNameAndPrice(final String newsName, final String price) {
        return new BloombergNewsBody(newsName, "+ 123", true, price, "+ 0.2%");
    }
}
