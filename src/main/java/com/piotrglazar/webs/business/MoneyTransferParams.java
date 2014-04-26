package com.piotrglazar.webs.business;

import java.math.BigDecimal;

public class MoneyTransferParams {

    private final String username;

    private final String email;

    private final Long fromAccount;

    private final Long toAccount;

    private final BigDecimal amount;

    public MoneyTransferParams(String username, String email, Long fromAccount, Long toAccount, BigDecimal amount) {
        this.username = username;
        this.email = email;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public Long getFromAccount() {
        return fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getEmail() {
        return email;
    }
}
