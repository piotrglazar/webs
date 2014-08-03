package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class IndexControllerContextTest extends AbstractContextTest {

    @Test
    public void shouldDisplayNewsAndWelcomeMessage() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/index").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//h1[@id = 'welcomeMessage']").exists())
            .andExpect(xpath("//div[contains(@class, 'item')]").nodeCount(2));
    }
}
