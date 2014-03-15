package com.piotrglazar.webs.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public final class SavingsAccount extends Account {

    protected BigDecimal interest;

    public SavingsAccount() {
    }

    public SavingsAccount(final String number, final Currency currency, final BigDecimal balance, final BigDecimal interest) {
        super(number, currency, balance);
        this.interest = interest;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(final BigDecimal interest) {
        this.interest = interest;
    }

    public static SavingsAccountBuilder builder() {
        return new SavingsAccountBuilder();
    }

}
