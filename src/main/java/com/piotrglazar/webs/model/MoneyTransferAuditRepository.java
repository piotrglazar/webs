package com.piotrglazar.webs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransferAuditRepository extends JpaRepository<MoneyTransferAudit, Long> {

}
