package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import com.piotrglazar.webs.business.MoneyTransferValidator;
import com.piotrglazar.webs.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
class CurrencyMoneyTransferValidator implements MoneyTransferValidator {

    @Override
    public void validate(final MoneyTransferDetails object, final List<String> errorGatherer) {
        final Account fromAccount = object.getAccountFrom();
        final Account toAccount = object.getAccountTo();

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            errorGatherer.add(format("Failed to transfer money from account %s in %s to account %s in %s",
                    fromAccount.getNumber(), fromAccount.getCurrency(), toAccount.getNumber(), toAccount.getCurrency()));
        }
    }
}
