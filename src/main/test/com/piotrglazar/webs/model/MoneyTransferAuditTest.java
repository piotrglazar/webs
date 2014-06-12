package com.piotrglazar.webs.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class MoneyTransferAuditTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(MoneyTransferAudit.class).verify();
    }
}
