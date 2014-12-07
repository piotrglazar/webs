package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.PasswordResetTokenRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.piotrglazar.webs.commons.Utils.addCsrf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PasswordResetControllerContextTest extends AbstractContextTest {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Test
    public void shouldCreateTokenAndRedirectToLoginPage() throws Exception {
        // given
        final WebsUser user = userProvider.findUserByUsername(Settings.USERNAME);

        // when
        mockMvc.perform(addCsrf(post("/resetPassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", user.getEmail())))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("login"));

        final List<PasswordResetToken> tokens = tokenRepository.findByIsExpiredAndUserId(false, user.getId());
        assertThat(tokens).isNotEmpty();

        // cleanup
        tokenRepository.deleteAll();
    }
}
