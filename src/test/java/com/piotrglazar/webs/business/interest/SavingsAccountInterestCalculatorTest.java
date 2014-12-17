package com.piotrglazar.webs.business.interest;

import com.piotrglazar.webs.InterestCalculationStrategy;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class SavingsAccountInterestCalculatorTest {

    @Mock
    private InterestCalculationStrategy strategy;

    @InjectMocks
    private SavingsAccountInterestCalculator calculator;

    @Test
    public void shouldSupportSavingsAccount() {
        // when
        final boolean supports = calculator.supports(AccountType.SAVINGS);

        // then
        assertThat(supports).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenAskedToCalculateInterestForNonSavingsAccount() {
        // given
        final Account nonSavingsAccount = mock(Account.class);

        // when
        calculator.calculateInterest(nonSavingsAccount);
    }

    @Test
    public void shouldUseCalculationStrategyToCalculateInterest() {
        // given
        final BigDecimal interestRate = new BigDecimal(5.0);
        final BigDecimal balance = new BigDecimal(1000);
        final SavingsAccount savingsAccount = savingsAccountWithInterestRateAndBalance(interestRate, balance);
        given(strategy.interest(balance, interestRate)).willReturn(BigDecimal.TEN);

        // when
        final BigDecimal interest = calculator.calculateInterest(savingsAccount);

        // then
        assertThat(interest).isEqualTo(BigDecimal.TEN);
    }

    private SavingsAccount savingsAccountWithInterestRateAndBalance(final BigDecimal interest, final BigDecimal balance) {
        return new SavingsAccount("account number", Currency.GBP, balance, interest);
    }
}
