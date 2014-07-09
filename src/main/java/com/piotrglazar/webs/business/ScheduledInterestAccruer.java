package com.piotrglazar.webs.business;

import com.piotrglazar.webs.config.BusinessConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledInterestAccruer {

    private final InterestAccruer interestAccruer;

    @Autowired
    public ScheduledInterestAccruer(InterestAccruer interestAccruer) {
        this.interestAccruer = interestAccruer;
    }

    @Scheduled(cron = BusinessConfiguration.INTEREST_RATE_ACCRUE_CRON_EXPRESSION)
    public void accrueInterestPeriodically() {
        interestAccruer.accrueInterest();
    }
}
