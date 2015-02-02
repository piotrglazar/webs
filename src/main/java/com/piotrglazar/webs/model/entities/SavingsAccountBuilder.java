package com.piotrglazar.webs.model.entities;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.business.utils.Currency;

import java.math.BigDecimal;
import java.util.List;

public class SavingsAccountBuilder {

    private Long id;
    private String number;
    private Currency currency = Currency.PLN;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal interest = BigDecimal.ZERO;
    private List<Subaccount> subaccounts = Lists.newLinkedList();

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

    public SavingsAccountBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public SavingsAccountBuilder subaccount(final Subaccount subaccount) {
        this.subaccounts.add(subaccount);
        return this;
    }

    public SavingsAccount build() {
        final SavingsAccount savingsAccount = new SavingsAccount(number, currency, balance, interest);
        if (id != null) {
            savingsAccount.setId(id);
        }
        subaccounts.forEach(savingsAccount::addSubaccount);
        return savingsAccount;
    }
}
