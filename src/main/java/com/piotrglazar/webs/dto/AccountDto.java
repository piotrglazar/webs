package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;

import java.math.BigDecimal;

public class AccountDto {

    private final Long id;
    private final String number;
    private final BigDecimal balance;
    private final Currency currency;

    public AccountDto(final Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }
}
