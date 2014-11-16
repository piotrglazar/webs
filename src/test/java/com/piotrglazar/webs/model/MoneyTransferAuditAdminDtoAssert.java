package com.piotrglazar.webs.model;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MoneyTransferAuditAdminDtoAssert {

    private final MoneyTransferAuditAdminDto that;

    public MoneyTransferAuditAdminDtoAssert(final MoneyTransferAuditAdminDto that) {
        this.that = that;
    }

    public static MoneyTransferAuditAdminDtoAssert assertThat(MoneyTransferAuditAdminDto dto) {
        return new MoneyTransferAuditAdminDtoAssert(dto);
    }

    public MoneyTransferAuditAdminDtoAssert hasAmount(BigDecimal amount) {
        Assertions.assertThat(that.getAmount()).isEqualByComparingTo(amount);
        return this;
    }

    public MoneyTransferAuditAdminDtoAssert hasReceivingAccountId(long receivingAccountId) {
        Assertions.assertThat(that.getReceivingAccountId()).isEqualTo(receivingAccountId);
        return this;
    }

    public MoneyTransferAuditAdminDtoAssert hasSendingAccountId(long sendingAccountId) {
        Assertions.assertThat(that.getSendingAccountId()).isEqualTo(sendingAccountId);
        return this;
    }

    public MoneyTransferAuditAdminDtoAssert hasSendingUserId(long sendingUserId) {
        Assertions.assertThat(that.getSendingUserId()).isEqualTo(sendingUserId);
        return this;
    }

    public MoneyTransferAuditAdminDtoAssert hasSuccess(boolean success) {
        Assertions.assertThat(that.getSuccess()).isEqualTo(success);
        return this;
    }

    public MoneyTransferAuditAdminDtoAssert hasDate(LocalDateTime date) {
        Assertions.assertThat(that.getDate()).isEqualTo(date);
        return this;
    }

    public MoneyTransferAuditAdminDtoAssert hasCurrency(final Currency currency) {
        Assertions.assertThat(that.getCurrency()).isEqualTo(currency);
        return this;
    }
}
