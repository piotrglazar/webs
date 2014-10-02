package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.InternalWebsNews;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class InternalWebsNewsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(InternalWebsNews.class).withRedefinedSuperclass().verify();
    }
}
