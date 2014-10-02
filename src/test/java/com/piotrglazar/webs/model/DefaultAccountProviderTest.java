package com.piotrglazar.webs.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.piotrglazar.webs.UniqueIdGenerator;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.AccountDtoFactory;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

    @Test
    public void shouldFailWhileCreatingNewAccountForNonExistingUser() {
        // given
        given(userProvider.getUserByUsername("user")).willThrow(new WebsUserNotFoundException("user"));

        // when
        catchException(accountProvider).newAccount("user", AccountType.SAVINGS, Currency.GBP);

        // then
        assertThat((Exception) caughtException()).isInstanceOf(WebsUserNotFoundException.class);
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
}
