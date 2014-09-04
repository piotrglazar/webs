package com.piotrglazar.webs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MoneyTransferAuditUserDtoBuilder {
    private MoneyTransferAuditUserDto.Kind kind = MoneyTransferAuditUserDto.Kind.INCOMING;
    private long userId;
    private BigDecimal amount = BigDecimal.ZERO;
    private boolean success;
    private LocalDateTime date = LocalDateTime.of(2014, 8, 31, 0, 0);
    private long accountId;

    public MoneyTransferAuditUserDtoBuilder kind(final MoneyTransferAuditUserDto.Kind kind) {
        this.kind = kind;
        return this;
    }

    public MoneyTransferAuditUserDtoBuilder userId(final long userId) {
        this.userId = userId;
        return this;
    }

    public MoneyTransferAuditUserDtoBuilder amount(final BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public MoneyTransferAuditUserDtoBuilder success(final boolean success) {
        this.success = success;
        return this;
    }

    public MoneyTransferAuditUserDtoBuilder date(final LocalDateTime date) {
        this.date = date;
        return this;
    }

    public MoneyTransferAuditUserDtoBuilder accountId(final long accountId) {
        this.accountId = accountId;
        return this;
    }

    public MoneyTransferAuditUserDto build() {
        return new MoneyTransferAuditUserDto(kind, userId, amount, success, date, accountId);
    }
}
