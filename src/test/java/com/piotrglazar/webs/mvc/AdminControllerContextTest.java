package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.commons.Utils;
import com.piotrglazar.webs.config.Settings;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerContextTest extends AbstractContextTest {

    @Before
    public void logout() {
        Utils.logout(mockMvc);
    }

    @Test
    public void shouldOrdinaryUserNotAccessAdminPage() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME2, Settings.PASSWORD);

        // when
        mockMvc.perform(get("/admin").session(authenticate))

        // then
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void shouldAdminUserAccessAdminPage() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc, Settings.USERNAME, Settings.PASSWORD);

        // when
        mockMvc.perform(get("/admin").session(authenticate))

        // then
            .andExpect(status().isOk());
    }
}
