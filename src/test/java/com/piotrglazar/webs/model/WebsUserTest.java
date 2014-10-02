package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.WebsUser;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class WebsUserTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(WebsUser.class).verify();
    }
}
