package com.piotrglazar.webs.business;

import com.piotrglazar.webs.converters.BigDecimalConverter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConvertParam;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DailyInterestCalculationStrategyTest {

    // reasonable defaults
    private AmountConverter amountConverter = new AmountConverter(2, RoundingMode.HALF_UP.toString());

    private DailyInterestCalculationStrategy strategy = new DailyInterestCalculationStrategy(amountConverter);

    @Test
    @Parameters({
            "100 | 0 | 0.00",
            "100 | 1 | 0.00",
            "1000000 | 1 | 27.40",
            "1000 | 5 | 0.14",
            "100 | 2 | 0.01"
    })
    public void shouldCalculateInterestAmount(@ConvertParam(BigDecimalConverter.class) BigDecimal amount,
                                              @ConvertParam(BigDecimalConverter.class) BigDecimal interestRate,
                                              @ConvertParam(BigDecimalConverter.class) BigDecimal expectedInterest) {
        // when
        final BigDecimal interest = strategy.interest(amount, interestRate);

        // then
        assertThat(interest).isEqualTo(amountConverter.convert(expectedInterest));
    }
}
