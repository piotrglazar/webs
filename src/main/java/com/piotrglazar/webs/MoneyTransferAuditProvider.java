package com.piotrglazar.webs;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.WebsPageable;
import rx.Observable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MoneyTransferAuditProvider {

    void auditMoneyTransfer(Long sendingUserId, Long sendingAccountId, Long receivingAccountId, BigDecimal amount, Boolean success,
                            LocalDateTime localDateTime, Long receiverUserId, Currency currency);

    List<MoneyTransferAuditAdminDto> findAll();

    WebsPageable<MoneyTransferAuditUserDto> findForUser(Long userId, int pageNumber);

    WebsPageable<MoneyTransferAuditUserDto> findPageForUsername(String username, int pageNumber);

    Observable<MoneyTransferAuditUserDto> findTransferHistory(String username);
}
