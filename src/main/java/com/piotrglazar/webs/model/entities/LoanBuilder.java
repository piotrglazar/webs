package com.piotrglazar.webs.model.entities;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.business.utils.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class LoanBuilder {

    private long id = 0;
    private BigDecimal amountLoaned = BigDecimal.ZERO;
    private BigDecimal amountRemaining = BigDecimal.ZERO;
    private Currency currency = Currency.PLN;
    private Set<LocalDate> postpones = Sets.newHashSet();
    private Account account;
    private Byte weeks = 0;
    private Byte weeksRemaining = 0;

    public LoanBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public LoanBuilder amountLoaned(final BigDecimal amountLoaned) {
        this.amountLoaned = amountLoaned;
        return this;
    }

    public LoanBuilder amountRemaining(final BigDecimal amountRemaining) {
        this.amountRemaining = amountRemaining;
        return this;
    }

    public LoanBuilder currency(final Currency currency) {
        this.currency = currency;
        return this;
    }

    public LoanBuilder postpones(final Set<LocalDate> postpones) {
        this.postpones = postpones;
        return this;
    }

    public LoanBuilder account(final Account account) {
        this.account = account;
        return this;
    }

    public LoanBuilder weeks(final Byte weeks) {
        this.weeks = weeks;
        return this;
    }

    public LoanBuilder weeksRemaining(final Byte weeksRemaining) {
        this.weeksRemaining = weeksRemaining;
        return this;
    }

    public Loan build() {
        Loan loan = new Loan();

        loan.setId(id);
        loan.setAccount(account);
        loan.setAmountLoaned(amountLoaned);
        loan.setAmountRemaining(amountRemaining);
        loan.setCurrency(currency);
        loan.setPostpones(postpones);
        loan.setWeeks(weeks);
        loan.setWeeksRemaining(weeksRemaining);

        return loan;
    }
}
