package com.piotrglazar.webs.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class WebsNewsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(WebsNews.class).verify();
    }
}
