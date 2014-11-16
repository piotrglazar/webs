package com.piotrglazar.webs;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.BloombergNewsBody;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsTemplatesTest extends AbstractContextTest {

    @Autowired
    private WebsTemplates websTemplates;

    @Test
    public void shouldWriteMessage() {
        // when
        final String template = websTemplates.mailMessage("user", 1L, 2L, BigDecimal.TEN, "receivingUser", Currency.GBP);

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

    private BloombergNewsBody createBloombergNewsBodyWithNameAndPrice(final String newsName, final String price) {
        return new BloombergNewsBody(newsName, "+ 123", true, price);
    }
}
