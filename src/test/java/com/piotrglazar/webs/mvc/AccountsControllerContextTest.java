package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.commons.Utils;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import com.piotrglazar.webs.model.entities.MoneyTransferAuditBuilder;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.model.repositories.MoneyTransferAuditRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.piotrglazar.webs.commons.Utils.addCsrf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class AccountsControllerContextTest extends AbstractContextTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MoneyTransferAuditRepository auditRepository;

    @Autowired
    private AccountProvider accountProvider;

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
        final List<String> validAccountNumbers = getAccountNumbersAlreadyInDb();

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

    @Test
    public void shouldDisplayAccountTransferHistory() throws Exception {
        // before
        final MoneyTransferAudit audit = saveMoneyTransferAudit();

        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/accountTransferHistory/0/").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("moneyTransferAuditData"))
            .andExpect(xpath("//table[@id='moneyTransferAuditTable']").exists())
            .andExpect(xpath("//li[@class='disabled']").nodeCount(2))
            .andExpect(xpath("//li[@class='active']").nodeCount(1));

        // cleanup
        removeMoneyTransferAudit(audit);
    }

    @Test
    public void shouldShowNewAccountCreationForm() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/newAccount").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("accountCreationForm", "allAccountTypes", "allCurrencies"));
    }

    @Test
    public void shouldServeMoneyTransferHistory() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        mockMvc.perform(get("/downloadTransferHistory").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("filename")))
            .andExpect(content().string(containsString(Settings.USERNAME)))
            .andExpect(content().string(containsString("content")));
    }

    @Test
    public void shouldCreateSubaccount() throws Exception {
        final Long accountId = getUsersAccountId(Settings.USERNAME);

        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/subaccountsCreate/" + accountId + "/").session(authenticate))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", accountId.toString())
                .param("subaccountName", "subaccount")
                .param("subaccountAmount", "10"))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/accounts/" + accountId + "/"));

        final Account account = accountRepository.findOne(accountId);
        assertThat(account.getSubaccounts()).hasSize(1);

        // cleanup
        accountProvider.deleteSubaccount(Settings.USERNAME, accountId, "subaccount");
    }

    @Test
    public void shouldDeleteSubaccount() throws Exception {
        final Long accountId = getUsersAccountId(Settings.USERNAME);

        // given
        accountProvider.newSubaccount(Settings.USERNAME, accountId, BigDecimal.TEN, "subaccount");
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(addCsrf(post("/subaccountsDelete/" + accountId + "/subaccount/").session(authenticate))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/accounts/" + accountId + "/"));

        final Account account = accountRepository.findOne(accountId);
        assertThat(account.getSubaccounts()).isEmpty();
    }

    private Long getUsersAccountId(String username) {
        return accountRepository.findByUsername(username).stream().map(Account::getId).findFirst().get();
    }

    private void removeMoneyTransferAudit(final MoneyTransferAudit audit) {
        auditRepository.delete(audit);
    }

    private MoneyTransferAudit saveMoneyTransferAudit() {
        final MoneyTransferAudit audit = new MoneyTransferAuditBuilder()
                .receivingUserId(1L)
                .receivingAccountId(1L)
                .build();
        auditRepository.save(audit);
        return audit;
    }

    private void removeAccountsOtherThan(final List<String> validAccountNumbers) {
        accountRepository.findAll().stream()
                .filter(a -> !validAccountNumbers.contains(a.getNumber()))
                .forEach(accountRepository::delete);
    }

    private List<String> getAccountNumbersAlreadyInDb() {
        return accountRepository.findAll().stream().map(Account::getNumber).collect(Collectors.toList());
    }
}
