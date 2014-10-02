package com.piotrglazar.webs.model;


import com.google.common.collect.Lists;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDtoBuilder;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDtoFactory;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.model.repositories.MoneyTransferAuditRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMoneyTransferAuditProviderTest {

    @Mock
    private MoneyTransferAuditUserDtoFactory factory;

    @Mock
    private UserProvider userProvider;

    @Mock
    private MoneyTransferAuditRepository repository;

    private int pageSize = 10;

    private DefaultMoneyTransferAuditProvider provider;

    @Test
    public void shouldConvertAllMoneyTransferAuditToDto() {
        // given
        provider = new DefaultMoneyTransferAuditProvider(repository, factory, userProvider, pageSize);
        given(repository.findAll()).willReturn(Lists.newArrayList(moneyTransferAuditEntry()));

        // when
        final List<MoneyTransferAuditAdminDto> dtos = provider.findAll();

        // then
        assertThat(dtos).hasSize(1);
        assertThatDtoWasCreatedFrom(dtos.get(0), moneyTransferAuditEntry());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldConvertMoneyTransferAuditToUserDtoForGivenUser() {
        // given
        provider = new DefaultMoneyTransferAuditProvider(repository, factory, userProvider, pageSize);
        // user id matches sendingUserId from moneyTransferAuditEntry
        final WebsUser user = new WebsUserBuilder().username("user").id(123L).build();
        given(userProvider.findUserByUsername("user")).willReturn(user);
        final MoneyTransferAudit auditEntry = moneyTransferAuditEntry();
        final Page<MoneyTransferAudit> page = mock(Page.class);
        given(page.getContent()).willReturn(Lists.newArrayList(auditEntry));
        given(repository.findBySendingUserIdOrReceivingUserId(123L, 123L, new PageRequest(0, 10))).willReturn(page);
        given(factory.build(123L, auditEntry)).willReturn(moneyTransferAuditUserDto());

        // when
        final WebsPageable<MoneyTransferAuditUserDto> dtos = provider.findPageForUsername("user", 0);

        // then
        assertThat(dtos.getContent()).hasSize(1);
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
        assertThat(moneyTransferAuditAdminDto.getAmount()).isEqualTo(audit.getAmount());
        assertThat(moneyTransferAuditAdminDto.getReceivingAccountId()).isEqualTo(audit.getReceivingAccountId());
        assertThat(moneyTransferAuditAdminDto.getSendingAccountId()).isEqualTo(audit.getSendingAccountId());
        assertThat(moneyTransferAuditAdminDto.getSendingUserId()).isEqualTo(audit.getSendingUserId());
        assertThat(moneyTransferAuditAdminDto.getSuccess()).isEqualTo(audit.getSuccess());
        assertThat(moneyTransferAuditAdminDto.getDate()).isEqualTo(audit.getDate());
    }

    private MoneyTransferAudit moneyTransferAuditEntry() {
        return new MoneyTransferAudit(123L, 10L, 15L, BigDecimal.ONE, true, LocalDateTime.of(2014, 5, 26, 22, 28), 100L);
    }
}
