package com.piotrglazar.webs.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.piotrglazar.webs.UniqueIdGenerator;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.WebsRuntimeException;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.AccountDtoFactory;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.dto.SubaccountDto;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.entities.SavingsAccountBuilder;
import com.piotrglazar.webs.model.entities.Subaccount;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountProviderTest {

    @Mock
    private AccountDtoFactory factory;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UniqueIdGenerator generator;

    @Mock
    private UserProvider userProvider;

    @InjectMocks
    private DefaultAccountProvider accountProvider;

    @Test
    public void shouldConvertSavingsAccountToDto() {
        // given
        final SavingsAccount savingsAccount = SavingsAccount.builder().number("abc").interest(BigDecimal.ONE).build();
        savingsAccount.setId(1L);
        given(accountRepository.findByUsername("user")).willReturn(Lists.<Account>newArrayList(savingsAccount));
        given(factory.dto(savingsAccount)).willReturn(new SavingsAccountDto(savingsAccount));

        // when
        final Optional<SavingsAccountDto> savingsAccountDto = accountProvider.getUserSavingsAccount("user", 1L);

        // then
        assertThat(savingsAccountDto.isPresent());
        assertThat(savingsAccountDto.get().getNumber()).isEqualTo("abc");
        assertThat(savingsAccountDto.get().getInterest()).isEqualByComparingTo("1.00");
    }

    @Test
    public void shouldReturnEmptySavingsAccountDto() {
        // given
        final Account account = mock(Account.class);
        given(account.getId()).willReturn(1L);
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(account));

        // when
        final Optional<SavingsAccountDto> savingsAccountDto = accountProvider.getUserSavingsAccount("user", 1L);

        // then
        assertThat(savingsAccountDto.isPresent()).isFalse();
    }

    @Test
    public void shouldConvertAllTypesOfAccountsToDto() {
        // only savings account exists for now
        // given
        final SavingsAccount savingsAccount = SavingsAccount.builder().number("abc").interest(BigDecimal.ONE).build();
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(savingsAccount));
        given(factory.dto(savingsAccount)).willReturn(new SavingsAccountDto(savingsAccount));

        // when
        final List<AccountDto> dtos = accountProvider.getUserAccounts("user");

        // then
        assertThat(dtos).hasSize(1).extracting("number").containsOnly("abc");
    }

    @Test
    public void shouldReturnEmptyListWhenNoAccountsFound() {
        // given
        given(accountRepository.findByUsername("user")).willReturn(Lists.newLinkedList());

        // when
        final List<AccountDto> dtos = accountProvider.getUserAccounts("user");

        // then
        assertThat(dtos).isEmpty();
    }

    @Test(expected = WebsUserNotFoundException.class)
    public void shouldFailWhileCreatingNewAccountForNonExistingUser() {
        // given
        given(userProvider.getUserByUsername("user")).willThrow(new WebsUserNotFoundException("user"));

        // when
        accountProvider.newAccount("user", AccountType.SAVINGS, Currency.GBP);
    }

    @Test(expected = WebsRuntimeException.class)
    public void shouldFailWhenCreatingAccountOfUnknownType() {
        // expected
        accountProvider.newAccount("user", null, Currency.GBP);
    }

    @Test
    public void shouldTryToFindUniqueAccountNumber() {
        // given
        HashSet<Account> accounts = Sets.newHashSet();
        WebsUser user = WebsUser.builder().accounts(accounts).build();
        Account account = mock(Account.class);
        given(userProvider.getUserByUsername("user")).willReturn(user);
        given(generator.generate()).willReturn("a", "b");
        given(accountRepository.findByNumber("a")).willReturn(account);

        // when
        accountProvider.newAccount("user", AccountType.SAVINGS, Currency.GBP);

        // then
        assertThat(accounts).hasSize(1);
        SavingsAccount savedAccount = (SavingsAccount) accounts.iterator().next();
        assertThat(savedAccount.getNumber()).isEqualTo("b");
        assertThat(savedAccount.getBalance()).isEqualByComparingTo("0");
        assertThat(savedAccount.getCurrency()).isEqualTo(Currency.GBP);
    }

    @Test
    public void shouldReturnAccountDtoWhenAccountNotFound() {
        // when
        final Optional<AccountDto> dto = accountProvider.getAccount("abc");

        // then
        assertThat(dto.isPresent()).isFalse();
    }

    @Test(expected = WebsAccountNotFoundException.class)
    public void shouldFailWhenCreatingSubaccountForNotExistingAccount() {
        // given
        given(accountRepository.findByUsername("user")).willReturn(Collections.emptyList());

        // when
        accountProvider.newSubaccount("user", 1, BigDecimal.TEN, "subaccount");
    }

    @Test
    public void shouldCreateSubaccount() {
        // given
        SavingsAccount savingsAccount = new SavingsAccountBuilder()
                .balance(BigDecimal.valueOf(100))
                .id(1)
                .build();
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(savingsAccount));

        // when
        accountProvider.newSubaccount("user", 1, BigDecimal.TEN, "subaccount");

        // then
        assertThat(savingsAccount.getSubaccounts()).hasSize(1);
        Subaccount subaccount = savingsAccount.getSubaccounts().get(0);
        assertThat(subaccount.getBalance()).isEqualTo(BigDecimal.TEN);
        assertThat(subaccount.getName()).isEqualTo("subaccount");
    }

    @Test(expected = DuplicateSubaccountNameException.class)
    public void shouldFailWhenThereIsAlreadyASubaccountWithTheSameName() {
        // given
        SavingsAccount savingsAccount = new SavingsAccountBuilder()
                .balance(BigDecimal.valueOf(100))
                .id(1)
                .subaccount(new Subaccount("subaccount", BigDecimal.TEN))
                .build();
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(savingsAccount));

        // when
        accountProvider.newSubaccount("user", 1, BigDecimal.valueOf(100), "subaccount");
    }

    @Test(expected = InsufficientFundsException.class)
    public void shouldFailWhenThereAreInsufficientFundsOnAccount() {
        // given
        SavingsAccount savingsAccount = new SavingsAccountBuilder()
                .balance(BigDecimal.ZERO)
                .id(1)
                .build();
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(savingsAccount));

        // when
        accountProvider.newSubaccount("user", 1, BigDecimal.valueOf(100), "subaccount");
    }

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoAccount() {
        // given
        given(accountRepository.findByUsername("user")).willReturn(Collections.emptyList());

        // when
        Optional<SubaccountDto> subaccount = accountProvider.getSubaccount("user", 1, "subaccount");

        // then
        assertThat(subaccount.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoSubaccount() {
        // given
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(new SavingsAccountBuilder().id(1).build()));

        // when
        Optional<SubaccountDto> subaccount = accountProvider.getSubaccount("user", 1, "subaccount");

        // then
        assertThat(subaccount.isPresent()).isFalse();
    }

    @Test
    public void shouldGetSubaccount() {
        // given
        Subaccount subaccount = new Subaccount("subaccount", BigDecimal.TEN);
        SavingsAccount account = new SavingsAccountBuilder().id(1).currency(Currency.GBP).subaccount(subaccount).build();
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(account));

        // when
        Optional<SubaccountDto> subaccountOpt = accountProvider.getSubaccount("user", 1, "subaccount");

        // then
        assertThat(subaccountOpt.isPresent());
        subaccountOpt.ifPresent(sub -> {
            assertThat(sub.getName()).isEqualTo("subaccount");
            assertThat(sub.getBalance()).isEqualTo(BigDecimal.TEN);
            assertThat(sub.getCurrency()).isEqualTo(Currency.GBP);
        });
    }

    @Test(expected = WebsAccountNotFoundException.class)
    public void shouldFailWhenThereIsNoAccountForGivenUser() {
        // given
        given(accountRepository.findByUsername("user")).willReturn(Collections.emptyList());

        // when
        accountProvider.deleteSubaccount("user", 1, "subaccount");
    }

    @Test(expected = WebsSubaccountNotFoundException.class)
    public void shouldFailWhenThereIsNoSubaccountForGivenUser() {
        // given
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(new SavingsAccountBuilder().id(1).build()));

        // when
        accountProvider.deleteSubaccount("user", 1, "subaccount");
    }

    @Test
    public void shouldDeleteSubaccount() {
        // given
        Subaccount subaccount = new Subaccount("subaccount", BigDecimal.TEN);
        SavingsAccount account = new SavingsAccountBuilder().id(1).balance(BigDecimal.valueOf(20)).subaccount(subaccount).build();
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(account));

        // when
        accountProvider.deleteSubaccount("user", 1, "subaccount");

        // then
        assertThat(account.getSubaccounts()).isEmpty();
        assertThat(account.getBalance()).isEqualByComparingTo("30");
        verify(accountRepository).saveAndFlush(account);
    }
}
