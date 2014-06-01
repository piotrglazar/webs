package com.piotrglazar.webs.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class WebsUserTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(WebsUser.class).verify();
    }
}
