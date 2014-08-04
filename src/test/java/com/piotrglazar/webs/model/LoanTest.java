package com.piotrglazar.webs.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class LoanTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(Loan.class).verify();
    }
}
