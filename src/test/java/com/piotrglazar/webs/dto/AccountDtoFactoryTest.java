package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.SavingsAccount;
import com.piotrglazar.webs.model.SavingsAccountBuilder;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

public class AccountDtoFactoryTest {

    private AccountDtoFactory factory = new AccountDtoFactory();

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenAskedForNonSavingsAccountDto() {
        // given
        final Account nonSavingsAccount = mock(Account.class);

        // expect
        factory.dto(nonSavingsAccount);
    }

    @Test
    public void shouldCreateDtoForSavingsAccount() {
        // given
        final SavingsAccount savingsAccount = new SavingsAccountBuilder()
                .currency(Currency.GBP)
                .interest(new BigDecimal("5.5"))
                .balance(BigDecimal.valueOf(1000))
                .number("abc")
                .build();

        // when
        final SavingsAccountDto dto = (SavingsAccountDto) factory.dto(savingsAccount);

        // then
        AccountDtoAssert.assertThatDto(dto)
                .hasBalance(savingsAccount.getBalance())
                .hasCurrency(savingsAccount.getCurrency())
                .hasInterest(savingsAccount.getInterest())
                .hasNumber(savingsAccount.getNumber());
    }
}