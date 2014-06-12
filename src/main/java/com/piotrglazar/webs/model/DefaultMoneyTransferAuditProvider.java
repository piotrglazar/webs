package com.piotrglazar.webs.model;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.dto.MoneyTransferAuditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
class DefaultMoneyTransferAuditProvider implements MoneyTransferAuditProvider {

    private final MoneyTransferAuditRepository repository;

    @Autowired
    public DefaultMoneyTransferAuditProvider(final MoneyTransferAuditRepository repository) {
        this.repository = repository;
    }

    @Override
    public void auditMoneyTransfer(Long sendingUserId, Long sendingAccountId, Long receivingAccountId, BigDecimal amount,
                                   Boolean success, LocalDateTime date) {
        final MoneyTransferAudit audit = new MoneyTransferAudit(sendingUserId, sendingAccountId, receivingAccountId, amount, success, date);

        repository.saveAndFlush(audit);
    }

    @Override
    public List<MoneyTransferAuditDto> findAll() {
        return repository.findAll().stream().map(MoneyTransferAuditDto::new).collect(Collectors.toList());
    }
}
