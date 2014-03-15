package com.piotrglazar.webs.model;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountProviderTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultAccountProvider accountProvider;

    @Test
    public void shouldConvertSavingsAccountToDto() {
        // given
        final SavingsAccount savingsAccount = SavingsAccount.builder().number("abc").interest(BigDecimal.ONE).build();
        savingsAccount.setId(1L);
        given(accountRepository.findByUsername("user")).willReturn(Lists.<Account>newArrayList(savingsAccount));

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
        // given
        final SavingsAccount savingsAccount = SavingsAccount.builder().number("abc").interest(BigDecimal.ONE).build();
        final Account newTypeAccount = mock(Account.class);
        given(newTypeAccount.getNumber()).willReturn("def");
        given(accountRepository.findByUsername("user")).willReturn(Lists.newArrayList(savingsAccount, newTypeAccount));

        // when
        final List<AccountDto> dtos = accountProvider.getUserAccounts("user");

        // then
        assertThat(dtos).hasSize(2).extracting("number").containsOnly("abc", "def");
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
}
