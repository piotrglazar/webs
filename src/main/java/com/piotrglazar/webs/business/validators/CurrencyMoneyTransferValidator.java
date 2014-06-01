package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import com.piotrglazar.webs.business.MoneyTransferValidator;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.util.ErrorGatherer;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
class CurrencyMoneyTransferValidator implements MoneyTransferValidator {

    @Override
    public void validate(final MoneyTransferDetails object, final ErrorGatherer errorGatherer) {
        final Account fromAccount = object.getAccountFrom();
        final Account toAccount = object.getAccountTo();

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            errorGatherer.reportError(format("Failed to transfer money from account %s in %s to account %s in %s",
                    fromAccount.getNumber(), fromAccount.getCurrency(), toAccount.getNumber(), toAccount.getCurrency()));
        }
    }
}
