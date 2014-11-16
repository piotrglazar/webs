package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Immutable
public final class MoneyTransferAuditUserDto {

    private final Kind kind;
    private final Long userId;
    private final BigDecimal amount;
    private final Boolean success;
    private final LocalDateTime date;
    private final Long accountId;
    private final Currency currency;

    public enum Kind {
        INCOMING, OUTGOING
    }

    public MoneyTransferAuditUserDto(Kind kind, Long userId, BigDecimal amount, Boolean success, LocalDateTime date, Long accountId,
                                     Currency currency) {
        this.kind = kind;
        this.userId = userId;
        this.amount = amount;
        this.success = success;
        this.date = date;
        this.accountId = accountId;
        this.currency = currency;
    }

    public Kind getKind() {
        return kind;
    }

    public Long getUserId() {
        return userId;
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

    public Long getAccountId() {
        return accountId;
    }

    public Currency getCurrency() {
        return currency;
    }
}
