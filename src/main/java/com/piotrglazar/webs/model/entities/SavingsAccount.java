package com.piotrglazar.webs.model.entities;

import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public final class SavingsAccount extends Account {

    @Column(nullable = false)
    protected BigDecimal interest;

    public SavingsAccount() {
    }

    @Override
    public AccountType accountType() {
        return AccountType.SAVINGS;
    }

    public SavingsAccount(String number, Currency currency, BigDecimal balance, BigDecimal interest) {
        super(number, currency, balance);
        this.interest = interest;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public static SavingsAccountBuilder builder() {
        return new SavingsAccountBuilder();
    }

}
