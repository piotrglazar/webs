package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class MoneyTransferAuditTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(MoneyTransferAudit.class).verify();
    }
}
