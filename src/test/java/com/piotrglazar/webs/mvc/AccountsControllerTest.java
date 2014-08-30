package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountType;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.SavingsAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

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
        given(loggedInUserProvider.getLoggedInUserUsername()).willReturn("user");
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
        verify(model).addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "accounts");
    }

    @Test
    public void shouldPopulateEmptyUserAccountListToViewWhenNoAccountsFound() {
        // given
        given(accountProvider.getUserAccounts("user")).willReturn(Lists.newLinkedList());

        // when
        accountsController.accounts(model);

        // then
        verify(model).addAttribute(eq("accounts"), any(List.class));
        verify(model).addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "accounts");
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

    @Test
    public void shouldRedirectToItselfWhenFormIsInvalid() {
        // given
        final AccountCreationForm form = mock(AccountCreationForm.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        final String redirect = accountsController.addAccount(form, bindingResult);

        // then
        assertThat(redirect).isEqualTo("newAccount");
        verify(bindingResult).hasErrors();
    }

    @Test
    public void shouldAddNewAccount() {
        // given
        final AccountCreationForm form = mock(AccountCreationForm.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        given(form.getCurrency()).willReturn(Currency.GBP);
        given(form.getType()).willReturn(AccountType.SAVINGS);

        // when
        final String redirect = accountsController.addAccount(form, bindingResult);

        // then
        assertThat(redirect).isEqualTo("redirect:/accounts");
        verify(accountProvider).newAccount(anyString(), eq(AccountType.SAVINGS), eq(Currency.GBP));
    }

    private List<AccountDto> createAccount() {
        final Account firstAccount = new SavingsAccount();
        firstAccount.setCurrency(Currency.PLN);
        firstAccount.setBalance(BigDecimal.TEN);
        firstAccount.setNumber("abc");

        return Lists.newArrayList(new AccountDto(firstAccount));
    }
}
