package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.commons.Utils;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.model.AccountType;
import com.piotrglazar.webs.model.Currency;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.stream.Collectors;

import static com.piotrglazar.webs.commons.Utils.addCsrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountsControllerContextTest extends AbstractContextTest {

    @Autowired
    private AccountRepository accountRepository;

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

    @Test
    public void shouldCreateAccount() throws Exception {
        // before
        List<String> validAccountNumbers = getAccountNumbersAlreadyInDb();

        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/newAccount").session(authenticate))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", AccountType.SAVINGS.name())
                .param("currency", Currency.GBP.name()))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/accounts"));

        // cleanup
        removeAccountsOtherThan(validAccountNumbers);
    }

    private void removeAccountsOtherThan(final List<String> validAccountNumbers) {
        accountRepository.findAll().stream()
                .filter(a -> !validAccountNumbers.contains(a.getNumber()))
                .forEach(accountRepository::delete);
    }

    private List<String> getAccountNumbersAlreadyInDb() {
        return accountRepository.findAll().stream().map(Account::getNumber).collect(Collectors.toList());
    }

    @Test
    public void shouldShowNewAccountCreationForm() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/newAccount").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("newAccountForm", "allAccountTypes", "allCurrencies"));
    }
}
