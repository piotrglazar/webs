package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import com.piotrglazar.webs.business.MoneyTransferValidator;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.util.ErrorGatherer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.lang.String.format;

@Component
class AccountBalanceMoneyTransferValidator implements MoneyTransferValidator {

    @Override
    public void validate(final MoneyTransferDetails object, final ErrorGatherer errorGatherer) {
        final Account fromAccount = object.getAccountFrom();
        final BigDecimal amount = object.getMoneyTransferAmount();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            errorGatherer.reportError(format("Failed to transfer %s from account %s - insufficient funds %s", amount,
                    fromAccount.getNumber(), fromAccount.getBalance()));
        }
    }
}
