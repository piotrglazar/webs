package com.piotrglazar.webs.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransferAuditRepository extends JpaRepository<MoneyTransferAudit, Long> {

    Page<MoneyTransferAudit> findBySendingUserIdOrReceivingUserId(Long sendingUserId, Long receivingUserId, Pageable pageable);
}
