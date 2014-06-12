package com.piotrglazar.webs;

import com.piotrglazar.webs.dto.MoneyTransferAuditDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MoneyTransferAuditProvider {

    void auditMoneyTransfer(Long sendingUserId, Long sendingAccountId, Long receivingAccountId, BigDecimal amount, Boolean success, final LocalDateTime localDateTime);

    List<MoneyTransferAuditDto> findAll();
}
