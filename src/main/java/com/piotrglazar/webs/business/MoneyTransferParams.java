package com.piotrglazar.webs.business;

import com.piotrglazar.webs.business.utils.Currency;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;

@Immutable
public final class MoneyTransferParams {

    private final String username;
    private final String email;
    private final Long fromAccount;
    private final Long toAccount;
    private final BigDecimal amount;
    private final Long receivingUserId;
    private final String receivingUsername;
    private final Currency currency;

    public MoneyTransferParams(String username, String email, Long fromAccount, Long toAccount, BigDecimal amount, Long receivingUserId,
                               String receivingUsername, Currency currency) {
        this.username = username;
        this.email = email;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.receivingUserId = receivingUserId;
        this.receivingUsername = receivingUsername;
        this.currency = currency;
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

    public Long getReceivingUserId() {
        return receivingUserId;
    }

    public String getReceivingUsername() {
        return receivingUsername;
    }

    public Currency getCurrency() {
        return currency;
    }
}
