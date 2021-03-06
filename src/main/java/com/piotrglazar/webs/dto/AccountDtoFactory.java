package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoFactory {

    public AccountDtoFactory() {

    }

    public AccountDto dto(Account account) {

        if (account instanceof SavingsAccount) {
            return new SavingsAccountDto((SavingsAccount) account);
        }

        throw new IllegalStateException("Unknown Account: " + account);
    }
}
