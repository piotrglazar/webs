package com.piotrglazar.webs.model;

import com.piotrglazar.webs.LoanCalculator;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import com.piotrglazar.webs.util.LoanOption;
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
public class LoanFactoryTest {

    @Mock
    private LoanCalculator loanCalculator;

    @InjectMocks
    private LoanFactory loanFactory;

    @Test
    public void shouldCreateLoan() {
        // given
        final Account account = mock(Account.class);
        final LoanCreationForm form = loanWithAmountLoanedAndOption("100", LoanOption.MEDIUM);
        given(loanCalculator.calculateLoan(form.getAmountLoaned(), form.getLoanOption())).willReturn(new BigDecimal("15"));
        given(account.getCurrency()).willReturn(Currency.GBP);

        // when
        final Loan loan = loanFactory.create(form, account);

        // then
        assertThat(loan.getAmountRemaining()).isEqualByComparingTo("115");
        assertThat(loan.getAccount()).isEqualTo(account);
        assertThat(loan.getAmountLoaned()).isEqualByComparingTo("100");
        assertThat(loan.getWeeksRemaining()).isEqualTo(LoanOption.MEDIUM.getWeeks());
        assertThat(loan.getWeeks()).isEqualTo(LoanOption.MEDIUM.getWeeks());
        assertThat(loan.getCurrency()).isEqualTo(Currency.GBP);
    }

    private LoanCreationForm loanWithAmountLoanedAndOption(final String amountLoaned, final LoanOption loanOption) {
        LoanCreationForm form = new LoanCreationForm();
        form.setAmountLoaned(new BigDecimal(amountLoaned));
        form.setLoanOption(loanOption);
        return form;
    }
}
