package com.piotrglazar.webs.model;

import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDtoFactory;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import com.piotrglazar.webs.model.entities.MoneyTransferAuditBuilder;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.MoneyTransferAuditRepository;
import com.piotrglazar.webs.util.MoreCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;

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
                                   Boolean success, LocalDateTime date, Long receivingUserId, Currency currency) {
        final MoneyTransferAudit audit = new MoneyTransferAuditBuilder()
            .sendingUserId(sendingUserId)
            .sendingAccountId(sendingAccountId)
            .receivingAccountId(receivingAccountId)
            .amount(amount)
            .success(success)
            .date(date)
            .receivingUserId(receivingUserId)
            .currency(currency)
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

    public Observable<MoneyTransferAuditUserDto> findTransferHistory(final String username) {
        final Long userId = userProvider.findUserByUsername(username).getId();
        return Observable.create((Observable.OnSubscribe<MoneyTransferAuditUserDto>) f -> {
            Page<MoneyTransferAudit> page = repository.findBySendingUserIdOrReceivingUserId(userId, userId,
                    new PageRequest(0, pageSize));
            subscribeAuditData(userId, f, page);
            f.onCompleted();
        });
    }

    private void subscribeAuditData(Long userId, Subscriber<? super MoneyTransferAuditUserDto> f, Page<MoneyTransferAudit> page) {
        page.getContent().stream().map(m -> factory.build(userId, m)).forEach(f::onNext);
        if (page.hasNext()) {
            subscribeAuditData(userId, f, repository.findBySendingUserIdOrReceivingUserId(userId, userId,
                    new PageRequest(page.nextPageable().getPageNumber(), pageSize)));
        }
    }
}
