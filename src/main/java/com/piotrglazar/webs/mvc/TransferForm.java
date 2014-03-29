package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.mvc.validators.PositiveNumber;
import com.piotrglazar.webs.mvc.validators.PositiveNumberLessThan100;
import org.hibernate.validator.constraints.NotBlank;

public class TransferForm {

    @PositiveNumber
    private long accountId;

    @NotBlank(message = "Please provide account number")
    private String accountNumber;

    @PositiveNumber
    private long integralPart;

    @PositiveNumberLessThan100
    private long fractionalPart;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(final long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getIntegralPart() {
        return integralPart;
    }

    public void setIntegralPart(final long integralPart) {
        this.integralPart = integralPart;
    }

    public long getFractionalPart() {
        return fractionalPart;
    }

    public void setFractionalPart(final long fractionalPart) {
        this.fractionalPart = fractionalPart;
    }
}
