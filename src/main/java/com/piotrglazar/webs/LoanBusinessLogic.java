package com.piotrglazar.webs;

import com.piotrglazar.webs.model.entities.Loan;

import java.math.BigDecimal;

public interface LoanBusinessLogic {

    boolean canPostpone(Loan loan);

    BigDecimal amountToPay(Loan loan);
}
