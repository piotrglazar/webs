package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.MoneyTransferAudit;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Immutable
public final class MoneyTransferAuditAdminDto {

    private final Long sendingUserId;
    private final Long sendingAccountId;
    private final Long receivingUserId;
    private final Long receivingAccountId;
    private final BigDecimal amount;
    private final Boolean success;
    private final LocalDateTime date;
    private final Currency currency;

    public MoneyTransferAuditAdminDto(MoneyTransferAudit audit) {
        this.sendingUserId = audit.getSendingUserId();
        this.sendingAccountId = audit.getSendingAccountId();
        this.receivingUserId = audit.getReceivingUserId();
        this.receivingAccountId = audit.getReceivingAccountId();
        this.amount = audit.getAmount();
        this.success = audit.getSuccess();
        this.date = audit.getDate();
        this.currency = audit.getCurrency();
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

    public Long getReceivingUserId() {
        return receivingUserId;
    }

    public Currency getCurrency() {
        return currency;
    }
}
