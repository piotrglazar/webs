package com.piotrglazar.webs.commons;

import com.piotrglazar.webs.infra.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Glazar
 * @since 24.02.14
 */
public class Utils {

    public static final String LOGIN_PAGE = "/login";

    private static final HttpSessionCsrfTokenRepository TOKEN_REPOSITORY = new HttpSessionCsrfTokenRepository();
    private static final String TOKEN_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    private static final MockHttpServletRequest anyRequest = new MockHttpServletRequest();
    private static MockHttpSession authenticatedSession;


    public static MockHttpSession authenticate(final MockMvc mockMvc) {
        try {
            return getSession(mockMvc.perform(postRequest()));
        } catch (final Exception e) {
            throw new RuntimeException("Failed to perform authentication", e);
        }
    }

    private static MockHttpSession getSession(final ResultActions resultActions) throws Exception {
        if (authenticatedSession == null) {
            authenticatedSession = (MockHttpSession) resultActions.andExpect(status().is(HttpStatus.FOUND.value()))
                                                    .andReturn().getRequest().getSession();
        }
        return authenticatedSession;
    }

    private static MockHttpServletRequestBuilder postRequest() {
        final CsrfToken token = TOKEN_REPOSITORY.generateToken(anyRequest);
        return post(LOGIN_PAGE)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", Settings.USERNAME)
                    .param("password", Settings.PASSWORD)
                    .param("_csrf", token.getToken())
                    .sessionAttr(TOKEN_NAME, token);
    }
}
