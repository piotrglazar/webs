package com.piotrglazar.webs.business;

import java.math.BigDecimal;

public class MoneyTransferParamsBuilder {

    private String username = "";
    private String email = "";
    private long fromAccount;
    private long toAccount;
    private BigDecimal amount = BigDecimal.ZERO;
    private long receivingUserId;
    private String receivingUsername = "";

    public MoneyTransferParamsBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public MoneyTransferParamsBuilder email(final String email) {
        this.email = email;
        return this;
    }

    public MoneyTransferParamsBuilder fromAccount(final long fromAccount) {
        this.fromAccount = fromAccount;
        return this;
    }

    public MoneyTransferParamsBuilder toAccount(final long toAccount) {
        this.toAccount = toAccount;
        return this;
    }

    public MoneyTransferParamsBuilder amount(final BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public MoneyTransferParamsBuilder receivingUserId(final long receivingUserId) {
        this.receivingUserId = receivingUserId;
        return this;
    }

    public MoneyTransferParamsBuilder receivingUsername(final String receivingUsername) {
        this.receivingUsername = receivingUsername;
        return this;
    }

    public MoneyTransferParams build() {
        return new MoneyTransferParams(username, email, fromAccount, toAccount, amount, receivingUserId, receivingUsername);
    }
}
