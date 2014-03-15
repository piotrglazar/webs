package com.piotrglazar.webs.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class SavingsAccountTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(SavingsAccount.class).withRedefinedSuperclass().verify();
    }
}
