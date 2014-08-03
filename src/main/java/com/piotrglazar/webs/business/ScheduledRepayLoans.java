package com.piotrglazar.webs.business;

import com.piotrglazar.webs.config.BusinessConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ScheduledRepayLoans {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LoanRepays loanRepays;

    @Autowired
    public ScheduledRepayLoans(final LoanRepays loanRepays) {
        this.loanRepays = loanRepays;
    }

    @Scheduled(cron = BusinessConfiguration.LOAN_REPAY_CRON_EXPRESSION)
    public void repayLoansPeriodically() {
        LOG.info("Started - repay loans");

        loanRepays.repayAllLoans();

        LOG.info("Ended - repay loans");
    }
}
