package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.Account;

import java.math.BigDecimal;

public class MoneyTransferDetails {

    private final Account accountTo;

    private final Account accountFrom;

    private final MoneyTransferParams params;


    public MoneyTransferDetails(final Account accountTo, final Account accountFrom, final MoneyTransferParams params) {
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.params = params;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public MoneyTransferParams getParams() {
        return params;
    }

    public BigDecimal getMoneyTransferAmount() {
        return params.getAmount();
    }
}
