package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.model.AccountType;
import com.piotrglazar.webs.model.Currency;

public class AccountCreationForm {

    private AccountType type;

    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(final AccountType type) {
        this.type = type;
    }
}
