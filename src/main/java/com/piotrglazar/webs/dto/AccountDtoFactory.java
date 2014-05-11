package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.SavingsAccount;

public class AccountDtoFactory {

    private AccountDtoFactory() {

    }

    public static AccountDto dto(Account account) {

        if (account instanceof SavingsAccount) {
            return new SavingsAccountDto((SavingsAccount) account);
        }

        throw new IllegalStateException("Unknown Account: " + account);
    }
}
