package com.piotrglazar.webs.util.beans;

import com.piotrglazar.webs.business.interest.InterestAccruer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "webs:name=InterestAccruing")
public class InterestAccruingBean {

    private final InterestAccruer interestAccruer;

    @Autowired
    public InterestAccruingBean(InterestAccruer interestAccruer) {
        this.interestAccruer = interestAccruer;
    }

    @ManagedOperation
    public void accrueInterest() {
        interestAccruer.accrueInterest();
    }
}
