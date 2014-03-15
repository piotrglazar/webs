package com.piotrglazar.webs.commons;

import com.piotrglazar.webs.config.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Utils {

    public static final String LOGIN_PAGE = "/login";

    private static final HttpSessionCsrfTokenRepository TOKEN_REPOSITORY = new HttpSessionCsrfTokenRepository();
    private static final String TOKEN_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    private static final MockHttpServletRequest anyRequest = new MockHttpServletRequest();
    private static MockHttpSession authenticatedSession;

    public static MockHttpSession authenticate(final MockMvc mockMvc) {
        return authenticate(mockMvc, Settings.USERNAME, Settings.PASSWORD);
    }

    private static MockHttpSession authenticate(final MockMvc mockMvc, final String username, final String password) {
        try {
            return getSession(mockMvc.perform(postRequest(username, password)));
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

    private static MockHttpServletRequestBuilder postRequest(final String username, final String password) {
        return addCsrf(post(LOGIN_PAGE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password));
    }

    public static MockHttpServletRequestBuilder addCsrf(final MockHttpServletRequestBuilder requestBuilder) {
        final CsrfToken token = TOKEN_REPOSITORY.generateToken(anyRequest);
        return requestBuilder.param("_csrf", token.getToken())
                .sessionAttr(TOKEN_NAME, token);
    }
}
