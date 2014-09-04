package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AbstractContextTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsUserRepositoryContextTest extends AbstractContextTest {

    @Autowired
    private WebsUserRepository websUserRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void shouldFindUserByHisAccountId() {
        // given
        final long accountId = getAccountId();

        // when
        final WebsUser accountOwner = websUserRepository.findByAccountsId(accountId);

        // then
        assertThat(accountOwner).isNotNull();
        assertThat(accountOwner.getAccounts()).extracting("id").contains(accountId);
    }

    private long getAccountId() {
        final List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotEmpty();
        return accounts.get(0).getId();
    }
}