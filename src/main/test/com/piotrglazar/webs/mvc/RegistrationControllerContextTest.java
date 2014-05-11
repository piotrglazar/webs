package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.config.Settings;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.piotrglazar.webs.commons.Utils.addCsrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class RegistrationControllerContextTest extends AbstractContextTest {

    @Test
    public void shouldShowRegistrationForm() throws Exception {
        // when
        mockMvc.perform(get("/newUser"))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("registerForm"));
    }

    @Test
    public void shouldRedirectToLoginPageAfterSuccessfulRegistration() throws Exception {
        // when
        mockMvc.perform(addCsrf(post("/newUser")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "non existing user")
                .param("email", "p@p.pl")
                .param("password", "pass")
                .param("repeatPassword", "pass")))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("login"));
    }

    @Test
    public void shouldShowErrorWhenUsernameAlreadyTaken() throws Exception {
        // we assume here that there is user Settings.USERNAME

        mockMvc.perform(addCsrf(post("/newUser"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", Settings.USERNAME)
                .param("email", "p@p.pl")
                .param("password", "pass")
                .param("repeatPassword", "pass"))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'usernameError']").exists());
    }

    @Test
    public void shouldShowErrorWhenPasswordAndRepeatPasswordDoNotMatch() throws Exception {
        // when
        mockMvc.perform(addCsrf(post("/newUser"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("email", "p@p.pl")
                .param("password", "pass")
                .param("repeatPassword", "pass2"))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'passwordError']").exists())
            .andExpect(xpath("//p[@id = 'repeatPasswordError']").exists());
    }

    @Test
    public void shouldShowErrorWhenEmailIsInvalid() throws Exception {
        // when
        mockMvc.perform(addCsrf(post("/newUser"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("email", "wrongemail")
                .param("password", "pass")
                .param("repeatPassword", "pass"))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'emailError']").exists());
    }
}
