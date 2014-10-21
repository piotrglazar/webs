package com.piotrglazar.webs.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class BloombergNewsBodyTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(BloombergNewsBody.class).verify();
    }
}
