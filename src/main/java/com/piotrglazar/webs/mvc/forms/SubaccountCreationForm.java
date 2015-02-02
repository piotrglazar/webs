package com.piotrglazar.webs.mvc.forms;

import com.piotrglazar.webs.mvc.validators.PositiveNumber;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

public class SubaccountCreationForm {

    @PositiveNumber
    private long accountId;

    @NotBlank
    private String subaccountName;

    @PositiveNumber
    private BigDecimal subaccountAmount;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(final long accountId) {
        this.accountId = accountId;
    }

    public String getSubaccountName() {
        return subaccountName;
    }

    public void setSubaccountName(final String subaccountName) {
        this.subaccountName = subaccountName;
    }

    public BigDecimal getSubaccountAmount() {
        return subaccountAmount;
    }

    public void setSubaccountAmount(final BigDecimal subaccountAmount) {
        this.subaccountAmount = subaccountAmount;
    }
}
