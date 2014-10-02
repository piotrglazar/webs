package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableSet;
import com.piotrglazar.webs.business.utils.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class LoanDto {

    private final BigDecimal amountLoaned;
    private final BigDecimal amountRemaining;
    private final Currency currency;
    private final Set<LocalDate> postpones;
    private final long accountId;
    private final String accountNumber;
    private final byte weeks;
    private final byte weeksRemaining;
    private final boolean canPostpone;
    private final long loanId;

    public LoanDto(BigDecimal amountLoaned, BigDecimal amountRemaining, Currency currency, Set<LocalDate> postpones, long accountId,
                   String accountNumber, byte weeks, byte weeksRemaining, boolean canPostpone, long loanId) {
        this.amountLoaned = amountLoaned;
        this.amountRemaining = amountRemaining;
        this.currency = currency;
        this.canPostpone = canPostpone;
        this.postpones = ImmutableSet.copyOf(postpones);
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.weeks = weeks;
        this.weeksRemaining = weeksRemaining;
        this.loanId = loanId;
    }

    public BigDecimal getAmountLoaned() {
        return amountLoaned;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Set<LocalDate> getPostpones() {
        return postpones;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public byte getWeeks() {
        return weeks;
    }

    public byte getWeeksRemaining() {
        return weeksRemaining;
    }

    public boolean getCanPostpone() {
        return canPostpone;
    }

    public long getLoanId() {
        return loanId;
    }
}
