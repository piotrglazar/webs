package com.piotrglazar.webs;

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
        final String template = websTemplates.mailMessage("user", 1L, 2L, BigDecimal.TEN);

        // then
        assertThat(template).isNotEmpty().contains("user", "1", "2", "10");
    }
}
