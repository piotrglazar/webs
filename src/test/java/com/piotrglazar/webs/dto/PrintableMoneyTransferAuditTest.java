package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PrintableMoneyTransferAuditTest {

    @Test
    public void shouldPrepareHumanReadableMessageForIncomingTransferAudit() {
        // given
        MoneyTransferAuditUserDto dto = new MoneyTransferAuditUserDtoBuilder()
                .accountId(123)
                .amount(new BigDecimal("777.7"))
                .date(LocalDateTime.of(2014, 10, 15, 0, 0))
                .kind(MoneyTransferAuditUserDto.Kind.INCOMING)
                .success(true)
                .userId(1001)
                .currency(Currency.GBP)
                .build();

        // when
        final PrintableMoneyTransferAudit printableMoneyTransferAudit = PrintableMoneyTransferAudit.from(dto);

        // then
        assertThat(printableMoneyTransferAudit.getMessage())
                .isEqualTo("On 2014-10-15T00:00 you have received 777.7 GBP from user 1001 to account 123 - Success");
    }

    @Test
    public void shouldPrepareHumanReadableMessageForOutgoingTransferAudit() {
        // given
        MoneyTransferAuditUserDto dto = new MoneyTransferAuditUserDtoBuilder()
                .accountId(123)
                .amount(new BigDecimal("777.7"))
                .date(LocalDateTime.of(2014, 10, 15, 0, 0))
                .kind(MoneyTransferAuditUserDto.Kind.OUTGOING)
                .success(false)
                .userId(1001)
                .currency(Currency.PLN)
                .build();

        // when
        final PrintableMoneyTransferAudit printableMoneyTransferAudit = PrintableMoneyTransferAudit.from(dto);

        // then
        assertThat(printableMoneyTransferAudit.getMessage())
                .isEqualTo("On 2014-10-15T00:00 you have sent 777.7 PLN to user 1001 from account 123 - Failure");
    }
}
