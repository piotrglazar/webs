package com.piotrglazar.webs.business.validators;

import com.piotrglazar.webs.business.MoneyTransferDetails;
import com.piotrglazar.webs.business.MoneyTransferValidator;
import com.piotrglazar.webs.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class AccountMoneyTransferValidator implements MoneyTransferValidator {

    @Override
    public void validate(final MoneyTransferDetails object, final List<String> errorGatherer) {
        final Account fromAccount = object.getAccountFrom();
        final Account toAccount = object.getAccountTo();

        boolean existingAccounts = true;

        if (fromAccount == null) {
            errorGatherer.add("Cannot transfer money from null account");
            existingAccounts = false;
        }

        if (toAccount == null) {
            errorGatherer.add("Cannot transfer money to null account");
            existingAccounts = false;
        }

        if (existingAccounts && fromAccount.getId() == toAccount.getId()) {
            errorGatherer.add("Cannot transfer money from account to itself");
        }
    }
}
