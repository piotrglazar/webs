package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.util.ErrorGatherer;

import java.math.BigDecimal;

public class MoneyTransferValidatorTest {

    protected ErrorGatherer errors = new ErrorGatherer();

    protected Account accountFrom = new SavingsAccount("accountNumber", Currency.PLN, BigDecimal.valueOf(20), BigDecimal.ZERO);

    protected Account accountTo = new SavingsAccount("accountNumber", Currency.PLN, BigDecimal.valueOf(20), BigDecimal.ZERO);

    protected MoneyTransferParams params = new MoneyTransferParams("user", "email", 1L, 2L, BigDecimal.TEN, 1L, "user2");
}
