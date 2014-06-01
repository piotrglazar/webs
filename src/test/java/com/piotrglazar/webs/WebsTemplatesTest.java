package com.piotrglazar.webs;


import com.piotrglazar.webs.config.UtilityConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UtilityConfiguration.class, WebsTemplates.class})
public class WebsTemplatesTest {

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
