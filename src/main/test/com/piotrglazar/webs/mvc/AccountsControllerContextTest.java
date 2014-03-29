package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.commons.Utils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountsControllerContextTest extends AbstractContextTest {

    @Autowired
    private AccountProvider provider;

    @Test
    public void shouldDisplayUserAccounts() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/accounts").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("accounts"));
    }

    @Test
    public void shouldDisplayAccountDetails() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);
        final long accountId = 1;

        // when
        mockMvc.perform(get(String.format("/accounts/%s/", accountId)).session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("accountDetails"));
    }

    @Test
    public void shouldRedirectToAccountsPageWhenAskedAboutNotExistingAccount() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);
        final long notExistingAccountId = 123;

        // when
        mockMvc.perform(get(String.format("/accounts/%s/", notExistingAccountId)).session(authenticate))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/accounts"));
    }
}
