package com.piotrglazar.webs.dto;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class LoanDtoBuilder {

    private BigDecimal amountLoaned;
    private BigDecimal amountRemaining;
    private Currency currency;
    private Set<LocalDate> postpones = Sets.newHashSet();
    private long accountId;
    private String accountNumber;
    private byte weeks;
    private byte weeksRemaining;
    private boolean canPostpone;
    private long loanId;

    public LoanDtoBuilder withAmountLoaned(final BigDecimal amountLoaned) {
        this.amountLoaned = amountLoaned;
        return this;
    }

    public LoanDtoBuilder withAmountRemaining(final BigDecimal remaining) {
        this.amountRemaining = remaining;
        return this;
    }

    public LoanDtoBuilder withCurrency(final Currency currency) {
        this.currency = currency;
        return this;
    }

    public LoanDtoBuilder withPostpones(final Set<LocalDate> postpones) {
        this.postpones = postpones;
        return this;
    }

    public LoanDtoBuilder withAccountId(final long accountId) {
        this.accountId = accountId;
        return this;
    }

    public LoanDtoBuilder withAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public LoanDtoBuilder withWeeks(final byte weeks) {
        this.weeks = weeks;
        return this;
    }

    public LoanDtoBuilder withWeeksRemaining(final byte weeksRemaining) {
        this.weeksRemaining = weeksRemaining;
        return this;
    }

    public LoanDtoBuilder withCanPostpone(final boolean canPostpone) {
        this.canPostpone = canPostpone;
        return this;
    }

    public LoanDtoBuilder withLoanId(final long loanId) {
        this.loanId = loanId;
        return this;
    }

    public LoanDto build() {
        return new LoanDto(amountLoaned, amountRemaining, currency, postpones, accountId, accountNumber, weeks, weeksRemaining,
                canPostpone, loanId);
    }

    public static LoanDto fromLoan(final Loan loan) {
        return new LoanDtoBuilder()
                .withAccountNumber(loan.getAccount().getNumber())
                .withAccountId(loan.getAccount().getId())
                .withAmountLoaned(loan.getAmountLoaned())
                .withCurrency(loan.getCurrency())
                .withPostpones(loan.getPostpones())
                .withAmountRemaining(loan.getAmountRemaining())
                .withWeeks(loan.getWeeks())
                .withWeeksRemaining(loan.getWeeksRemaining())
                .withCanPostpone(loan.getCanPostpone())
                .withLoanId(loan.getId())
                .build();
    }
}
