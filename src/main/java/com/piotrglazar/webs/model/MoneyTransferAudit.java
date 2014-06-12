package com.piotrglazar.webs.model;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public final class MoneyTransferAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long sendingUserId;

    private Long sendingAccountId;

    private Long receivingAccountId;

    private BigDecimal amount;

    private Boolean success;

    private LocalDateTime date;

    public MoneyTransferAudit() {

    }

    public MoneyTransferAudit(Long sendingUserId, Long sendingAccountId, Long receivingAccountId, BigDecimal amount,
                              Boolean success, LocalDateTime date) {
        this.sendingUserId = sendingUserId;
        this.sendingAccountId = sendingAccountId;
        this.receivingAccountId = receivingAccountId;
        this.amount = amount;
        this.success = success;
        this.date = date;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getSendingUserId() {
        return sendingUserId;
    }

    public void setSendingUserId(final Long sendingUserId) {
        this.sendingUserId = sendingUserId;
    }

    public Long getSendingAccountId() {
        return sendingAccountId;
    }

    public void setSendingAccountId(final Long sendingAccountId) {
        this.sendingAccountId = sendingAccountId;
    }

    public Long getReceivingAccountId() {
        return receivingAccountId;
    }

    public void setReceivingAccountId(final Long receivingAccountId) {
        this.receivingAccountId = receivingAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MoneyTransferAudit other = (MoneyTransferAudit) obj;
        return Objects.equal(this.id, other.id);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(final LocalDateTime date) {
        this.date = date;
    }
}
