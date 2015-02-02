package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;

import java.math.BigDecimal;

public class SubaccountDto {

    private final String name;
    private final BigDecimal balance;
    private final Currency currency;

    public SubaccountDto(String name, BigDecimal balance, Currency currency) {
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }
}
