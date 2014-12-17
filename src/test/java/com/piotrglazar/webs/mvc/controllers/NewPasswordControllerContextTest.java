package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.passwordreset.PasswordResetTokenService;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.WebsUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.piotrglazar.webs.commons.Utils.addCsrf;
import static com.piotrglazar.webs.commons.Utils.authenticate;
import static com.piotrglazar.webs.commons.Utils.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewPasswordControllerContextTest extends AbstractContextTest {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Test
    public void shouldResetUserPassword() throws Exception {
        // given
        final WebsUser user = userProvider.findUserByUsername(Settings.USERNAME);
        final PasswordResetToken passwordResetToken = passwordResetTokenService.generateAndStoreTokenForUser(user);

        // when
        mockMvc.perform(addCsrf(post("/resetPassword/" + passwordResetToken.getTokenId() + "/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("newPassword", "newPassword"))
                .param("repeatedNewPassword", "newPassword"))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/login"));

        // and try to log in with new password
        logout(mockMvc);
        authenticate(mockMvc, user.getUsername(), "newPassword");

        // cleanup
        logout(mockMvc);
        userProvider.updateUserPassword(user, user.getPassword());
    }
}
