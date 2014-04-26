package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import com.piotrglazar.webs.model.Currency;
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
        assertThat(errors).isEmpty();
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
        assertThat(errors).isNotEmpty();
    }
}
