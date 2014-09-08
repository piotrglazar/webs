package com.piotrglazar.webs.mvc;

import com.google.common.base.MoreObjects;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LoanCreationForm {

    @NotNull
    @DecimalMin("1")
    private BigDecimal amountLoaned;

    @NotNull
    private LoanOption loanOption;

    @NotNull
    @Min(1)
    private Long accountId;

    public BigDecimal getAmountLoaned() {
        return amountLoaned;
    }

    public void setAmountLoaned(final BigDecimal amountLoaned) {
        this.amountLoaned = amountLoaned;
    }

    public LoanOption getLoanOption() {
        return loanOption;
    }

    public void setLoanOption(final LoanOption loanOption) {
        this.loanOption = loanOption;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(final Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("amountLoaned", amountLoaned)
                .add("loanOption", loanOption)
                .add("accountId", accountId)
                .toString();
    }
}
