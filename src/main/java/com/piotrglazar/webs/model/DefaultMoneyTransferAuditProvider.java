package com.piotrglazar.webs.model;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDtoFactory;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.util.MoreCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
class DefaultMoneyTransferAuditProvider implements MoneyTransferAuditProvider {

    private final int pageSize;
    private final MoneyTransferAuditRepository repository;
    private final UserProvider userProvider;
    private final MoneyTransferAuditUserDtoFactory factory;

    @Autowired
    public DefaultMoneyTransferAuditProvider(MoneyTransferAuditRepository repository, MoneyTransferAuditUserDtoFactory factory,
                                             UserProvider userProvider, @Value("#{businessProperties['page.size']?:10}") int pageSize) {
        this.repository = repository;
        this.userProvider = userProvider;
        this.factory = factory;
        this.pageSize = pageSize;
    }

    @Override
    public void auditMoneyTransfer(Long sendingUserId, Long sendingAccountId, Long receivingAccountId, BigDecimal amount,
                                   Boolean success, LocalDateTime date, Long receivingUserId) {
        final MoneyTransferAudit audit = new MoneyTransferAuditBuilder()
                                                .sendingUserId(sendingUserId)
                                                .sendingAccountId(sendingAccountId)
                                                .receivingAccountId(receivingAccountId)
                                                .amount(amount)
                                                .success(success)
                                                .date(date)
                                                .receivingUserId(receivingUserId)
                                                .build();

        repository.saveAndFlush(audit);
    }

    @Override
    public List<MoneyTransferAuditAdminDto> findAll() {
        return repository.findAll().stream().map(MoneyTransferAuditAdminDto::new).collect(MoreCollectors.toImmutableList());
    }

    @Override
    public WebsPageable<MoneyTransferAuditUserDto> findForUser(final Long userId, final int pageNumber) {
        final Page<MoneyTransferAudit> page = repository.findBySendingUserIdOrReceivingUserId(userId, userId,
                new PageRequest(pageNumber, pageSize));
        return new WebsPageable<>(page).transform(m -> factory.build(userId, m));
    }

    @Override
    public WebsPageable<MoneyTransferAuditUserDto> findPageForUsername(final String username, final int pageNumber) {
        final WebsUser user = userProvider.findUserByUsername(username);
        return findForUser(user.getId(), pageNumber);
    }
}
