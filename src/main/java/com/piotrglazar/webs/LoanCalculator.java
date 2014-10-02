package com.piotrglazar.webs;

import com.piotrglazar.webs.util.LoanOption;

import java.math.BigDecimal;

public interface LoanCalculator {

    BigDecimal calculateLoan(BigDecimal amountLoaned, LoanOption loanOption);
}
