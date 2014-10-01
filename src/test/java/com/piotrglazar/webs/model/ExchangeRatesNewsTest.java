package com.piotrglazar.webs.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class ExchangeRatesNewsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(ExchangeRatesNews.class).withRedefinedSuperclass().verify();
    }
}
