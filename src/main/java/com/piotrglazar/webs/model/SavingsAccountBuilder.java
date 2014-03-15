package com.piotrglazar.webs.model;

import java.math.BigDecimal;

public class SavingsAccountBuilder {

    private String number;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal interest;

    public SavingsAccountBuilder number(final String number) {
        this.number = number;
        return this;
    }

    public SavingsAccountBuilder currency(final Currency currency) {
        this.currency = currency;
        return this;
    }

    public SavingsAccountBuilder balance(final BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public SavingsAccountBuilder interest(final BigDecimal interest) {
        this.interest = interest;
        return this;
    }

    public SavingsAccount build() {
        return new SavingsAccount(number, currency, balance, interest);
    }
}
