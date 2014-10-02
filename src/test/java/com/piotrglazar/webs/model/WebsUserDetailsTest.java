package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.WebsUserDetails;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class WebsUserDetailsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(WebsUserDetails.class).verify();
    }
}
