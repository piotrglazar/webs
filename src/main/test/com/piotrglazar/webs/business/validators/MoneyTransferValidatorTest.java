package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Currency;
import com.piotrglazar.webs.model.SavingsAccount;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class MoneyTransferValidatorTest {

    protected List<String> errors = new LinkedList<>();

    protected Account accountFrom = new SavingsAccount("accountNumber", Currency.PLN, BigDecimal.valueOf(20), BigDecimal.ZERO);

    protected Account accountTo = new SavingsAccount("accountNumber", Currency.PLN, BigDecimal.valueOf(20), BigDecimal.ZERO);

    protected MoneyTransferParams params = new MoneyTransferParams("user", "email", 1L, 2L, BigDecimal.TEN);
}
