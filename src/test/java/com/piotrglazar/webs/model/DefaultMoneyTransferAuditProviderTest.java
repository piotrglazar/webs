package com.piotrglazar.webs.model;


import com.google.common.collect.Lists;
import com.piotrglazar.webs.dto.MoneyTransferAuditDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMoneyTransferAuditProviderTest {

    @Mock
    private MoneyTransferAuditRepository repository;

    @InjectMocks
    private DefaultMoneyTransferAuditProvider provider;

    @Test
    public void shouldConvertAllMoneyTransferAuditToDto() {
        // given
        given(repository.findAll()).willReturn(Lists.newArrayList(moneyTransferAuditEntry()));

        // when
        final List<MoneyTransferAuditDto> dtos = provider.findAll();

        // then
        assertThat(dtos).hasSize(1);
        assertThatDtoWasCreatedFrom(dtos.get(0), moneyTransferAuditEntry());
    }

    private void assertThatDtoWasCreatedFrom(final MoneyTransferAuditDto moneyTransferAuditDto, final MoneyTransferAudit audit) {
        assertThat(moneyTransferAuditDto.getAmount()).isEqualTo(audit.getAmount());
        assertThat(moneyTransferAuditDto.getReceivingAccountId()).isEqualTo(audit.getReceivingAccountId());
        assertThat(moneyTransferAuditDto.getSendingAccountId()).isEqualTo(audit.getSendingAccountId());
        assertThat(moneyTransferAuditDto.getSendingUserId()).isEqualTo(audit.getSendingUserId());
        assertThat(moneyTransferAuditDto.getSuccess()).isEqualTo(audit.getSuccess());
        assertThat(moneyTransferAuditDto.getDate()).isEqualTo(audit.getDate());
    }

    private MoneyTransferAudit moneyTransferAuditEntry() {
        return new MoneyTransferAudit(1L, 10L, 15L, BigDecimal.ONE, true, LocalDateTime.of(2014, 5, 26, 22, 28));
    }
}
