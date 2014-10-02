package com.piotrglazar.webs.business;

import com.piotrglazar.webs.LoanCalculator;
import com.piotrglazar.webs.util.LoanOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class DefaultLoanCalculator implements LoanCalculator {

    public static final int WEEKS_IN_MONTH = 4;

    private final TimePeriodCalculationStrategy strategy;

    @Autowired
    public DefaultLoanCalculator(TimePeriodCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public BigDecimal calculateLoan(final BigDecimal amountLoaned, final LoanOption loanOption) {
        return strategy.interest(amountLoaned, loanOption.getPercentage(), loanOption.getWeeks() / WEEKS_IN_MONTH);
    }
}
