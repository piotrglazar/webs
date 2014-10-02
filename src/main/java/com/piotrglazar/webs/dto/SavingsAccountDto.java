package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.entities.SavingsAccount;

import java.math.BigDecimal;

public class SavingsAccountDto extends AccountDto {

    private final BigDecimal interest;

    public SavingsAccountDto(final SavingsAccount savingsAccount) {
        super(savingsAccount);
        this.interest = savingsAccount.getInterest();
    }

    public BigDecimal getInterest() {
        return interest;
    }
}
