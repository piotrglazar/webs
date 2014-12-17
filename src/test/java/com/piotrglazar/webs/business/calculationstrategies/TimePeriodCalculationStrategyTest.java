package com.piotrglazar.webs.business.calculationstrategies;

import com.piotrglazar.webs.business.calculationstrategies.TimePeriodCalculationStrategy;
import com.piotrglazar.webs.business.utils.AmountConverter;
import com.piotrglazar.webs.converters.BigDecimalConverter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConvertParam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class TimePeriodCalculationStrategyTest {

    @Mock
    private AmountConverter amountConverter;

    @InjectMocks
    private TimePeriodCalculationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        given(amountConverter.getScale()).willReturn(2);
        given(amountConverter.getRoundingMode()).willReturn(RoundingMode.HALF_UP);
    }

    @Test
    @Parameters({
            "1000 | 5 | 12 | 50.00",
            "1000 | 5 | 6 | 25.00",
            "1000 | 5 | 1 | 4.17",
    })
    public void shouldCalculateInterestMonthly(@ConvertParam(BigDecimalConverter.class) BigDecimal initialAmount,
                                               @ConvertParam(BigDecimalConverter.class) BigDecimal interestRate, int months,
                                               @ConvertParam(BigDecimalConverter.class) BigDecimal expectedInterest) {
        // given
        expectedInterest = expectedInterest.setScale(amountConverter.getScale(), amountConverter.getRoundingMode());

        // when
        final BigDecimal interest = strategy.interest(initialAmount, interestRate, months);

        // then
        assertThat(interest).isEqualTo(expectedInterest);
    }
}
