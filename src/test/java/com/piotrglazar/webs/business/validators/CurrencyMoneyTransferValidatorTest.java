package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.moneytransfer.MoneyTransferDetails;
import com.piotrglazar.webs.business.utils.Currency;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyMoneyTransferValidatorTest extends MoneyTransferValidatorTest {

    private CurrencyMoneyTransferValidator validator = new CurrencyMoneyTransferValidator();

    @Test
    public void shouldAllowMoneyTransfer() {
        // given
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(accountTo, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isTrue();
    }

    @Test
    public void shouldFailWhenTryingToTransferMoneyFromPlnToGbp() {
        // given
        accountFrom.setCurrency(Currency.PLN);
        accountTo.setCurrency(Currency.GBP);
        final MoneyTransferDetails moneyTransferDetails = new MoneyTransferDetails(accountTo, accountFrom, params);

        // when
        validator.validate(moneyTransferDetails, errors);

        // then
        assertThat(errors.isEmpty()).isFalse();
    }
}
