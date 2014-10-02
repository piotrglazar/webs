package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import com.piotrglazar.webs.business.MoneyTransferValidator;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.util.ErrorGatherer;
import org.springframework.stereotype.Component;

@Component
class AccountMoneyTransferValidator implements MoneyTransferValidator {

    @Override
    public void validate(final MoneyTransferDetails object, final ErrorGatherer errorGatherer) {
        final Account fromAccount = object.getAccountFrom();
        final Account toAccount = object.getAccountTo();

        boolean existingAccounts = true;

        if (fromAccount == null) {
            errorGatherer.reportError("Cannot transfer money from null account");
            existingAccounts = false;
        }

        if (toAccount == null) {
            errorGatherer.reportError("Cannot transfer money to null account");
            existingAccounts = false;
        }

        if (existingAccounts && fromAccount.getId().equals(toAccount.getId())) {
            errorGatherer.reportError("Cannot transfer money from account to itself");
        }
    }
}
