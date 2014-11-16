package com.piotrglazar.webs.model;


import com.google.common.collect.Lists;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDtoBuilder;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDtoFactory;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.model.repositories.MoneyTransferAuditRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rx.Observable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.piotrglazar.webs.TestUtilities.toListToBlocking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class DefaultMoneyTransferAuditProviderTest {

    @Mock
    private MoneyTransferAuditUserDtoFactory factory;

    @Mock
    private UserProvider userProvider;

    @Mock
    private MoneyTransferAuditRepository repository;

    @Mock
    private Page<MoneyTransferAudit> page = mock(Page.class);

    private int pageSize = 10;

    private DefaultMoneyTransferAuditProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new DefaultMoneyTransferAuditProvider(repository, factory, userProvider, pageSize);
    }

    @Test
    public void shouldConvertAllMoneyTransferAuditToDto() {
        // given
        given(repository.findAll()).willReturn(Lists.newArrayList(moneyTransferAuditEntry()));

        // when
        final List<MoneyTransferAuditAdminDto> dtos = provider.findAll();

        // then
        assertThat(dtos).hasSize(1);
        assertThatDtoWasCreatedFrom(dtos.get(0), moneyTransferAuditEntry());
    }

    @Test
    public void shouldConvertMoneyTransferAuditToUserDtoForGivenUser() {
        // given
        // user id matches sendingUserId from moneyTransferAuditEntry
        userWithNameAndId("user", 123L);
        final MoneyTransferAudit auditEntry = auditEntry();
        pageWithAuditData();
        auditDataForUser(auditEntry);

        // when
        final WebsPageable<MoneyTransferAuditUserDto> dtos = provider.findPageForUsername("user", 0);

        // then
        assertThat(dtos.getContent()).hasSize(1);
    }

    private void auditDataForUser(final MoneyTransferAudit auditEntry) {
        given(factory.build(123L, auditEntry)).willReturn(moneyTransferAuditUserDto());
    }

    @Test
    public void shouldBuildObservableWithTransferHistoryData() {
        // given
        // user id matches sendingUserId from moneyTransferAuditEntry
        userWithNameAndId("user", 123L);
        final MoneyTransferAudit auditEntry = auditEntry();
        pageWithAuditData();
        auditDataForUser(auditEntry);

        // when
        final Observable<MoneyTransferAuditUserDto> transferHistory = provider.findTransferHistory("user");

        // then
        final List<MoneyTransferAuditUserDto> dto = toListToBlocking(transferHistory);
        // there are two page objects
        assertThat(dto).hasSize(2);
    }

    private void pageWithAuditData() {
        given(repository.findBySendingUserIdOrReceivingUserId(123L, 123L, new PageRequest(0, pageSize))).willReturn(page);
    }

    private MoneyTransferAudit auditEntry() {
        final MoneyTransferAudit auditEntry = moneyTransferAuditEntry();
        given(page.getContent()).willReturn(Lists.newArrayList(auditEntry));
        given(page.hasNext()).willReturn(true, false);
        given(page.nextPageable()).willReturn(mock(Pageable.class));
        return auditEntry;
    }

    private void userWithNameAndId(final String username, final long id) {
        final WebsUser user = new WebsUserBuilder().username(username).id(id).build();
        given(userProvider.findUserByUsername(username)).willReturn(user);
    }

    private MoneyTransferAuditUserDto moneyTransferAuditUserDto() {
        return new MoneyTransferAuditUserDtoBuilder()
                    .kind(MoneyTransferAuditUserDto.Kind.INCOMING)
                    .userId(1)
                    .amount(BigDecimal.ONE)
                    .success(true)
                    .date(LocalDateTime.of(2014, 9, 3, 0, 0))
                    .accountId(123)
                    .build();
    }

    private void assertThatDtoWasCreatedFrom(final MoneyTransferAuditAdminDto moneyTransferAuditAdminDto, final MoneyTransferAudit audit) {
        MoneyTransferAuditAdminDtoAssert.assertThat(moneyTransferAuditAdminDto)
                .hasAmount(audit.getAmount())
                .hasReceivingAccountId(audit.getReceivingAccountId())
                .hasSendingAccountId(audit.getSendingAccountId())
                .hasSendingUserId(audit.getSendingUserId())
                .hasSuccess(audit.getSuccess())
                .hasDate(audit.getDate())
                .hasCurrency(audit.getCurrency());
    }

    private MoneyTransferAudit moneyTransferAuditEntry() {
        return new MoneyTransferAudit(123L, 10L, 15L, BigDecimal.ONE, true, LocalDateTime.of(2014, 5, 26, 22, 28), 100L, Currency.GBP);
    }
}
