package com.piotrglazar.webs.business.calculationstrategies;

import com.piotrglazar.webs.InterestCalculationStrategy;
import com.piotrglazar.webs.business.utils.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DailyInterestCalculationStrategy implements InterestCalculationStrategy {

    private static final BigDecimal DAYS_IN_YEAR = new BigDecimal(365);
    private static final BigDecimal PERCENTAGE_DIVISOR = BigDecimal.valueOf(100);

    private final AmountConverter amountConverter;

    @Autowired
    public DailyInterestCalculationStrategy(final AmountConverter amountConverter) {
        this.amountConverter = amountConverter;
    }

    @Override
    public BigDecimal interest(final BigDecimal amount, final BigDecimal interestRate) {
       return amount
                .multiply(interestRate)
                .divide(PERCENTAGE_DIVISOR, amountConverter.getScale(), amountConverter.getRoundingMode())
                .divide(DAYS_IN_YEAR, amountConverter.getScale(), amountConverter.getRoundingMode());
    }
}
