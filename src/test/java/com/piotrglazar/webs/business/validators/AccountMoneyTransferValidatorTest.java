package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountMoneyTransferValidatorTest extends MoneyTransferValidatorTest {

    private AccountMoneyTransferValidator validator = new AccountMoneyTransferValidator();

    @Test
    public void shouldAllowMoneyTransfer() {
        // given
        accountFrom.setId(1);
        accountTo.setId(2);
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(accountTo, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isTrue();
    }

    @Test
    public void shouldFailWhenAccountFromIsNull() {
        // given
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(accountTo, null, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isFalse();
    }

    @Test
    public void shouldFailWhenAccountToIsNull() {
        // given
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(null, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isFalse();
    }

    @Test
    public void shouldFailWhenTryingToTransferMoneyFromAccountToItself() {
        // given
        accountFrom.setId(1);
        accountTo.setId(1);
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(accountTo, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isFalse();
    }
}
