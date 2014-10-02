package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import org.springframework.stereotype.Component;

@Component
public class MoneyTransferAuditUserDtoFactory {

    public MoneyTransferAuditUserDtoFactory() {

    }

    public MoneyTransferAuditUserDto build(Long ownerId, MoneyTransferAudit moneyTransferAudit) {
        MoneyTransferAuditUserDto.Kind kind = calculateKind(ownerId, moneyTransferAudit);
        Long userId = getOtherUserIdBasingOnKind(kind, moneyTransferAudit);
        Long accountId = getAffectedAccountIdBasingOnKind(kind, moneyTransferAudit);
        return new MoneyTransferAuditUserDtoBuilder()
                .kind(kind)
                .userId(userId)
                .amount(moneyTransferAudit.getAmount())
                .success(moneyTransferAudit.getSuccess())
                .date(moneyTransferAudit.getDate())
                .accountId(accountId)
                .build();
    }

    private Long getAffectedAccountIdBasingOnKind(MoneyTransferAuditUserDto.Kind kind, MoneyTransferAudit moneyTransferAudit) {
        if (kind == MoneyTransferAuditUserDto.Kind.INCOMING) {
            return moneyTransferAudit.getReceivingAccountId();
        } else {
            return moneyTransferAudit.getSendingAccountId();
        }
    }

    private Long getOtherUserIdBasingOnKind(MoneyTransferAuditUserDto.Kind kind, MoneyTransferAudit moneyTransferAudit) {
        if (kind == MoneyTransferAuditUserDto.Kind.INCOMING) {
            return moneyTransferAudit.getSendingUserId();
        } else {
            return moneyTransferAudit.getReceivingUserId();
        }
    }

    private MoneyTransferAuditUserDto.Kind calculateKind(final Long ownerId, final MoneyTransferAudit moneyTransferAudit) {
        if (moneyTransferAudit.getSendingUserId().equals(ownerId)) {
            return MoneyTransferAuditUserDto.Kind.OUTGOING;
        } else if (moneyTransferAudit.getReceivingUserId().equals(ownerId)) {
            return MoneyTransferAuditUserDto.Kind.INCOMING;
        } else {
            throw new IllegalArgumentException(String.format("Expecting %s to be either sender %s or receiver %s", ownerId,
                    moneyTransferAudit.getSendingUserId(), moneyTransferAudit.getReceivingUserId()));
        }
    }
}
