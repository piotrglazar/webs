package com.piotrglazar.webs.business.calculationstrategies;

import com.piotrglazar.webs.business.utils.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TimePeriodCalculationStrategy {

    private static final BigDecimal PERCENTAGE_DIVISOR = BigDecimal.valueOf(100);
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private final AmountConverter amountConverter;

    @Autowired
    public TimePeriodCalculationStrategy(final AmountConverter amountConverter) {
        this.amountConverter = amountConverter;
    }

    public BigDecimal interest(BigDecimal initialAmount, BigDecimal interestRate, int months) {
        // we assume that interestRate describes yearly interest rate

        return initialAmount
                .multiply(interestRate)
                .multiply(new BigDecimal(months))
                .divide(PERCENTAGE_DIVISOR, amountConverter.getScale(), amountConverter.getRoundingMode())
                .divide(MONTHS_IN_YEAR, amountConverter.getScale(), amountConverter.getRoundingMode());
    }
}
