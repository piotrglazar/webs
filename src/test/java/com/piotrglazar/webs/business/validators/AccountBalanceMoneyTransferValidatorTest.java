package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.moneytransfer.MoneyTransferDetails;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountBalanceMoneyTransferValidatorTest extends MoneyTransferValidatorTest {

    private AccountBalanceMoneyTransferValidator validator = new AccountBalanceMoneyTransferValidator();

    @Test
    public void shouldFailWhenInsufficientFundsOnAccounts() {
        // given
        accountFrom.setBalance(BigDecimal.ONE);
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(null, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isFalse();
    }

    @Test
    public void shouldAllowToTransferMoney() {
        // given
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(accountTo, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isTrue();
    }
}
