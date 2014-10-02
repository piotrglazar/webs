package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;

public class AccountDtoAssert {

    private final SavingsAccountDto accountDto;

    public AccountDtoAssert(final SavingsAccountDto accountDto) {
        this.accountDto = accountDto;
    }

    public static AccountDtoAssert assertThatDto(final SavingsAccountDto accountDto) {
        return new AccountDtoAssert(accountDto);
    }

    public AccountDtoAssert hasNumber(final String number) {
        Assertions.assertThat(number).isEqualTo(accountDto.getNumber());
        return this;
    }

    public AccountDtoAssert hasBalance(final BigDecimal balance) {
        Assertions.assertThat(balance).isEqualTo(accountDto.getBalance());
        return this;
    }

    public AccountDtoAssert hasInterest(final BigDecimal interest) {
        Assertions.assertThat(interest).isEqualTo(accountDto.getInterest());
        return this;
    }

    public AccountDtoAssert hasCurrency(final Currency currency) {
        Assertions.assertThat(currency).isEqualTo(accountDto.getCurrency());
        return this;
    }
}
