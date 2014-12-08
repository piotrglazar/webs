package com.piotrglazar.webs.model.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class PasswordResetTokenTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(PasswordResetToken.class).verify();
    }
}
