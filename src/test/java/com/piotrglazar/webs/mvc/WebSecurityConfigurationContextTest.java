package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebSecurityConfigurationContextTest extends AbstractContextTest {

    @Test
    public void shouldRequireAuthentication() throws Exception {
        // given

        // when
        mockMvc.perform(get("/index"))

        // then
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void shouldAuthenticatedUserAccessSecuredContent() throws Exception {
        // given
        final MockHttpSession authentication = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/index").session(authentication))

        // then
            .andExpect(status().isOk());
    }
}
