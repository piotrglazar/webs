package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.config.Settings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        // given

        // when
        mockMvc.perform(get("/newUser"))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("registerForm"));
    }

    @Test
    public void shouldRedirectToLoginPageAfterSuccessfulRegistration() throws Exception {
        // given

        // when
        mockMvc.perform(addCsrf(post("/newUser")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "non existing user")
                .param("password", "pass")
                .param("repeatPassword", "pass")))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("login"));
    }

    @Test
    public void shouldShowErrorWhenUsernameAlreadyTaken() throws Exception {
        // given
        // we assume here that there is user Settings.USERNAME

        // when
        mockMvc.perform(addCsrf(post("/newUser"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", Settings.USERNAME)
                .param("password", "pass")
                .param("repeatPassword", "pass"))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'usernameError']").exists());
    }

    @Test
    public void shouldShowErrorWhenPasswordAndRepeatPasswordDoNotMatch() throws Exception {
        // given

        // when
        mockMvc.perform(addCsrf(post("/newUser"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("password", "pass")
                .param("repeatPassword", "pass2"))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//p[@id = 'passwordError']").exists())
            .andExpect(xpath("//p[@id = 'repeatPasswordError']").exists());
    }
}
