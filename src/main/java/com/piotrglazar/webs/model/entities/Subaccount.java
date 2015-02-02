package com.piotrglazar.webs.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Subaccount {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal balance;

    public Subaccount() {
    }

    public Subaccount(final String name, final BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }
}
