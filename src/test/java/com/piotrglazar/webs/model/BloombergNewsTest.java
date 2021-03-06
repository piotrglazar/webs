package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.BloombergNews;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class BloombergNewsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(BloombergNews.class).withRedefinedSuperclass().verify();
    }
}
