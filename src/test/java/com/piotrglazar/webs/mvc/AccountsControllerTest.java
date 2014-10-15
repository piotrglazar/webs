package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.PagerDto;
import com.piotrglazar.webs.dto.PagerDtoFactory;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.dto.UserDownloads;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.mvc.controllers.AccountsController;
import com.piotrglazar.webs.mvc.forms.AccountCreationForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import rx.Observable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
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

    @Mock
    private MoneyTransferAuditProvider moneyTransferAuditProvider;

    @Mock
    private PagerDtoFactory pagerDtoFactory;

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
        final String accountsTemplate = accountsController.accounts(model);

        // then
        assertThat(accountsTemplate).isEqualTo("accounts");
        verify(model).addAttribute("accounts", accounts);
        verify(model).addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "accounts");
    }

    @Test
    public void shouldPopulateEmptyUserAccountListToViewWhenNoAccountsFound() {
        // given
        final LinkedList<AccountDto> dtos = Lists.newLinkedList();
        given(accountProvider.getUserAccounts("user")).willReturn(dtos);

        // when
        final String accountsTemplate = accountsController.accounts(model);

        // then
        assertThat(accountsTemplate).isEqualTo("accounts");
        verify(model).addAttribute("accounts", dtos);
        verify(model).addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "accounts");
    }

    @Test
    public void shouldDisplayAccountDetails() {
        // given
        final SavingsAccountDto dto = mock(SavingsAccountDto.class);
        given(accountProvider.getUserSavingsAccount("user", 1L)).willReturn(Optional.of(dto));

        // when
        final String accountDetailsTemplate = accountsController.accountDetails(1L, model);

        // then
        assertThat(accountDetailsTemplate).isEqualTo("accountDetails");
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

    @Test
    @SuppressWarnings("unchecked")
    public void shouldPopulateUserTransferAuditDetails() {
        // given
        final int pageNumber = 0;
        final WebsPageable websPageable = new WebsPageable(mock(Page.class));
        given(moneyTransferAuditProvider.findPageForUsername("user", pageNumber)).willReturn(websPageable);
        final LinkedList<PagerDto> pagers = Lists.newLinkedList();
        given(pagerDtoFactory.createPagers(any(Integer.class), any(Integer.class), any(String.class))).willReturn(pagers);
        final PagerDto pager = PagerDto.emptyPager();
        given(pagerDtoFactory.createLeftPager(any(Integer.class), any(Boolean.class), any(String.class))).willReturn(pager);
        given(pagerDtoFactory.createRightPager(any(Integer.class), any(Boolean.class), any(String.class))).willReturn(pager);

        // when
        final String transferHistoryTemplate = accountsController.displayAccountTransferHistory(pageNumber, model);

        // then
        assertThat(transferHistoryTemplate).isEqualTo("accountTransferHistory");
        verify(model).addAttribute("moneyTransferAuditData", websPageable);
        verify(model).addAttribute("pagers", pagers);
        verify(model).addAttribute("leftPager", pager);
        verify(model).addAttribute("rightPager", pager);
    }

    @Test
    public void shouldPrepareMoneyTransferDetailsData() {
        // given
        final MoneyTransferAuditUserDto dto = arbitraryMoneyTransferAuditUserDto();
        given(moneyTransferAuditProvider.findTransferHistory("user"))
                .willReturn(Observable.from(new MoneyTransferAuditUserDto[]{dto}));

        // when
        final UserDownloads auditData = accountsController.moneyTransferAuditData();

        // then
        assertThat(auditData.getFilename()).contains("user");
        assertThat(auditData.getContent()).hasSize(1);
        assertThat(auditData.getContent().get(0)).contains(dto.getAccountId().toString(), dto.getAmount().toString(),
                dto.getUserId().toString());
    }

    private MoneyTransferAuditUserDto arbitraryMoneyTransferAuditUserDto() {
        return new MoneyTransferAuditUserDto(MoneyTransferAuditUserDto.Kind.INCOMING, 123L, BigDecimal.TEN, Boolean.TRUE,
                LocalDateTime.of(2014, 10, 14, 0, 0), 10001L);
    }

    private List<AccountDto> createAccount() {
        final Account firstAccount = new SavingsAccount();
        firstAccount.setCurrency(Currency.PLN);
        firstAccount.setBalance(BigDecimal.TEN);
        firstAccount.setNumber("abc");

        return Lists.newArrayList(new AccountDto(firstAccount));
    }
}
