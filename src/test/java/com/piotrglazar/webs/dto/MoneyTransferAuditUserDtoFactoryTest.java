package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.MoneyTransferAudit;
import com.piotrglazar.webs.model.MoneyTransferAuditBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTransferAuditUserDtoFactoryTest {

    private MoneyTransferAuditUserDtoFactory factory = new MoneyTransferAuditUserDtoFactory();

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUnableToDetectKind() {
        // given
        final long ownerId = 123;
        final MoneyTransferAudit audit = auditWithSendingAndReceivingUserIds(100, 200);

        // when
        factory.build(ownerId, audit);
    }

    @Test
    public void shouldDetectIncomingKind() {
        // given
        final long ownerId = 100;
        final long sendingUserId = 200;
        final long receivingAccountId = 123;
        final MoneyTransferAudit audit = auditWithOwnerReceivingAccountAndSendingUser(ownerId, receivingAccountId, sendingUserId);

        // when
        final MoneyTransferAuditUserDto userDto = factory.build(ownerId, audit);

        // then
        assertThat(userDto.getKind()).isEqualTo(MoneyTransferAuditUserDto.Kind.INCOMING);
        assertThat(userDto.getAccountId()).isEqualTo(receivingAccountId);
        assertThat(userDto.getUserId()).isEqualTo(sendingUserId);
    }

    @Test
    public void shouldDetectOutgoingKind() {
        // given
        final long ownerId = 100;
        final long receivingUserId = 100;
        final long sendingAccountId = 123;
        final MoneyTransferAudit audit = auditWithOwnerSendingAccountAndReceiving(ownerId, sendingAccountId, receivingUserId);

        // when
        final MoneyTransferAuditUserDto userDto = factory.build(ownerId, audit);

        // then
        assertThat(userDto.getKind()).isEqualTo(MoneyTransferAuditUserDto.Kind.OUTGOING);
        assertThat(userDto.getAccountId()).isEqualTo(sendingAccountId);
        assertThat(userDto.getUserId()).isEqualTo(receivingUserId);
    }

    private MoneyTransferAudit auditWithOwnerSendingAccountAndReceiving(long ownerId, long sendingAccountId, long receivingUserId) {
        return new MoneyTransferAuditBuilder()
                .sendingUserId(ownerId)
                .sendingAccountId(sendingAccountId)
                .receivingUserId(receivingUserId)
                .build();
    }

    private MoneyTransferAudit auditWithOwnerReceivingAccountAndSendingUser(long ownerId, long receivingAccountId, long sendingUserId) {
        return new MoneyTransferAuditBuilder()
                .receivingUserId(ownerId)
                .receivingAccountId(receivingAccountId)
                .sendingUserId(sendingUserId)
                .build();
    }

    private MoneyTransferAudit auditWithSendingAndReceivingUserIds(final long sendingUserId, final long receivingUserId) {
        return new MoneyTransferAuditBuilder()
                .sendingUserId(sendingUserId)
                .receivingUserId(receivingUserId)
                .build();
    }
}