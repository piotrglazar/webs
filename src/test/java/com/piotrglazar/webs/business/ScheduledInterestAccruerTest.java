package com.piotrglazar.webs.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledInterestAccruerTest {
    
    @Mock
    private InterestAccruer interestAccruer;
    
    @InjectMocks
    private ScheduledInterestAccruer scheduledInterestAccruer;

    @Test
    public void shouldAccrueInterest() {
        // when
        scheduledInterestAccruer.accrueInterestPeriodically();

        // then
        verify(interestAccruer).accrueInterest();
    }
}
