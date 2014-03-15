package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.config.MvcConfig;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.SavingsAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountsControllerTest {

    @Mock
    private Model model;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @Mock
    private AccountProvider accountProvider;

    @InjectMocks
    private AccountsController accountsController;

    @Before
    public void logInUser() {
        given(loggedInUserProvider.getLoggedInUser()).willReturn(new User("user", "", Sets.newHashSet()));
    }

    @Test
    public void shouldPopulateUserAccountsToView() {
        // given
        final List<AccountDto> accounts = createAccount();
        given(accountProvider.getUserAccounts("user")).willReturn(accounts);

        // when
        accountsController.accounts(model);

        // then
        verify(model).addAttribute("accounts", accounts);
        verify(model).addAttribute(MvcConfig.PAGE_NAME_ATTRIBUTE, "accounts");
    }

    @Test
    public void shouldPopulateEmptyUserAccountListToViewWhenNoAccountsFound() {
        // given
        given(accountProvider.getUserAccounts("user")).willReturn(Lists.newLinkedList());

        // when
        accountsController.accounts(model);

        // then
        verify(model).addAttribute(eq("accounts"), any(List.class));
        verify(model).addAttribute(MvcConfig.PAGE_NAME_ATTRIBUTE, "accounts");
    }

    @Test
    public void shouldDisplayAccountDetails() {
        // given
        final SavingsAccountDto dto = mock(SavingsAccountDto.class);
        given(accountProvider.getUserSavingsAccount("user", 1L)).willReturn(Optional.of(dto));

        // when
        accountsController.accountDetails(1L, model);

        // then
        verify(model).addAttribute("accountDetails", dto);
    }

    @Test
    public void shouldRedirectToAccountsPageWhenAskedForNotExistingAccountDetails() {
        // given
        given(accountProvider.getUserSavingsAccount("user", 1L)).willReturn(Optional.empty());

        // when
        final String redirect = accountsController.accountDetails(1L, model);

        // then
        verify(model, never()).addAttribute(anyString(), any(SavingsAccountDto.class));
        assertThat(redirect).isEqualTo("redirect:/accounts");
    }

    private List<AccountDto> createAccount() {
        final Account firstAccount = new SavingsAccount();
        firstAccount.setCurrency(Currency.PLN);
        firstAccount.setBalance(BigDecimal.TEN);
        firstAccount.setNumber("abc");

        return Lists.newArrayList(new AccountDto(firstAccount));
    }
}
