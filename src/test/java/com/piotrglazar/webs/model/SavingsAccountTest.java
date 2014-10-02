package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.SavingsAccount;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class SavingsAccountTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(SavingsAccount.class).withRedefinedSuperclass().verify();
    }
}
