package com.piotrglazar.webs.util.beans;

import com.piotrglazar.webs.business.loans.LoanRepays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "webs:name=LoanRepaying")
public class LoanRepayingBean {

    private final LoanRepays loanRepays;

    @Autowired
    public LoanRepayingBean(LoanRepays loanRepays) {
        this.loanRepays = loanRepays;
    }

    @ManagedOperation
    public void repayLoans() {
        loanRepays.repayAllLoans();
    }
}
