package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.Loan;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class LoanTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(Loan.class).verify();
    }
}
