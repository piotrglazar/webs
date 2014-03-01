package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebSecurityConfigurationContextTest extends AbstractContextTest {

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain).build();
    }

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
