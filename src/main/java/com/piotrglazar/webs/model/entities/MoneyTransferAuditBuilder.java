package com.piotrglazar.webs.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MoneyTransferAuditBuilder {

    private long id;
    private long sendingUserId;
    private long sendingAccountId;
    private long receivingAccountId;
    private long receivingUserId;
    private BigDecimal amount = BigDecimal.ZERO;
    private boolean success = true;
    private LocalDateTime date = LocalDateTime.of(2014, 8, 31, 0, 0);

    public MoneyTransferAuditBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public MoneyTransferAuditBuilder sendingUserId(final long sendingUserId) {
        this.sendingUserId = sendingUserId;
        return this;
    }

    public MoneyTransferAuditBuilder sendingAccountId(final long sendingAccountId) {
        this.sendingAccountId = sendingAccountId;
        return this;
    }

    public MoneyTransferAuditBuilder receivingAccountId(final long receivingAccountId) {
        this.receivingAccountId = receivingAccountId;
        return this;
    }

    public MoneyTransferAuditBuilder receivingUserId(final long receivingUserId) {
        this.receivingUserId = receivingUserId;
        return this;
    }

    public MoneyTransferAuditBuilder amount(final BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public MoneyTransferAuditBuilder success(final boolean success) {
        this.success = success;
        return this;
    }

    public MoneyTransferAuditBuilder date(final LocalDateTime date) {
        this.date = date;
        return this;
    }

    public MoneyTransferAudit build() {
        final MoneyTransferAudit audit = new MoneyTransferAudit(sendingUserId, sendingAccountId, receivingAccountId, amount, success, date,
                receivingUserId);
        if (id != 0) {
            audit.setId(id);
        }

        return audit;
    }
}
