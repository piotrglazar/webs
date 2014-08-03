package com.piotrglazar.webs.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledRepayLoansTest {

    @Mock
    private LoanRepays loanRepays;

    @InjectMocks
    private ScheduledRepayLoans scheduledRepayLoans;

    @Test
    public void shouldRepayLoansPeriodically() {
        // when
        scheduledRepayLoans.repayLoansPeriodically();

        // then
        verify(loanRepays).repayAllLoans();
    }
}
