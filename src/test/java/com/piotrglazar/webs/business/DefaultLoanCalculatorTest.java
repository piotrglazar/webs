package com.piotrglazar.webs.business;

import com.piotrglazar.webs.mvc.LoanOption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLoanCalculatorTest {

    @Mock
    private TimePeriodCalculationStrategy strategy;

    @InjectMocks
    private DefaultLoanCalculator calculator;

    @Test
    public void shouldCalculateAmountToBePaid() {
        // given
        final BigDecimal amountLoaned = new BigDecimal("1000");
        final LoanOption loanOption = LoanOption.LONG;
        given(strategy.interest(amountLoaned, loanOption.getPercentage(), loanOption.getWeeks() / DefaultLoanCalculator.WEEKS_IN_MONTH))
                .willReturn(new BigDecimal("123"));

        // when
        final BigDecimal loan = calculator.calculateLoan(amountLoaned, loanOption);

        // then
        assertThat(loan).isEqualByComparingTo("123");
    }
}
