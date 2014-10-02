package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.ExchangeRatesNews;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class ExchangeRatesNewsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(ExchangeRatesNews.class).withRedefinedSuperclass().verify();
    }
}
