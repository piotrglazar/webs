package com.piotrglazar.webs.business.interest;

import com.piotrglazar.webs.config.BusinessConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ScheduledInterestAccruer {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InterestAccruer interestAccruer;

    @Autowired
    public ScheduledInterestAccruer(InterestAccruer interestAccruer) {
        this.interestAccruer = interestAccruer;
    }

    @Scheduled(cron = BusinessConfiguration.INTEREST_RATE_ACCRUE_CRON_EXPRESSION)
    public void accrueInterestPeriodically() {
        LOG.info("Started - interest accrue");

        interestAccruer.accrueInterest();

        LOG.info("Ended - interest accrue");
    }
}
