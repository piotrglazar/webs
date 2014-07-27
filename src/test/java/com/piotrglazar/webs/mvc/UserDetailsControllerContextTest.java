package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class UserDetailsControllerContextTest extends AbstractContextTest {

    @Test
    public void shouldShowUserDetails() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/userDetails").session(authenticate))

        // then
                .andExpect(status().isOk())
                .andExpect(xpath("//span[@id='username']").string("user"))
                .andExpect(xpath("//span[@id='email']").string("piotr.glazar@gmail.com"))
                .andExpect(xpath("//span[@id='address']").string("Warsaw, Poland"))
                .andExpect(xpath("//span[@id='memberSince']").exists());
    }
}
