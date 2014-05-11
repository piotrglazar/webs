package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.MoneyTransferAudit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MoneyTransferAuditDto {

    private final Long sendingUserId;
    private final Long sendingAccountId;
    private final Long receivingAccountId;
    private final BigDecimal amount;
    private final Boolean success;
    private final LocalDateTime date;

    public MoneyTransferAuditDto(MoneyTransferAudit audit) {
        this.sendingUserId = audit.getSendingUserId();
        this.sendingAccountId = audit.getSendingAccountId();
        this.receivingAccountId = audit.getReceivingAccountId();
        this.amount = audit.getAmount();
        this.success = audit.getSuccess();
        this.date = audit.getDate();
    }

    public Long getSendingUserId() {
        return sendingUserId;
    }

    public Long getSendingAccountId() {
        return sendingAccountId;
    }

    public Long getReceivingAccountId() {
        return receivingAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Boolean getSuccess() {
        return success;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
