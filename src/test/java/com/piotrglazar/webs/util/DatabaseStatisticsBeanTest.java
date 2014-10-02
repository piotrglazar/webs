package com.piotrglazar.webs.util;

import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.model.repositories.WebsUserRepository;
import com.piotrglazar.webs.util.beans.DatabaseStatisticsBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseStatisticsBeanTest {

    @Mock
    private WebsUserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DatabaseStatisticsBean statisticsBean;

    @Test
    public void shouldFetchUsersCountFromDb() {
        // given
        final long numberOfUsersInDb = 42;
        given(userRepository.count()).willReturn(numberOfUsersInDb);

        // when
        final long numberOfUsers = statisticsBean.getNumberOfUsers();

        // then
        verify(userRepository).count();
        assertThat(numberOfUsers).isEqualTo(numberOfUsersInDb);
    }

    @Test
    public void shouldFetchAccountsCountFromDb() {
        // given
        final long numberOfAccountsInDb = 123;
        given(accountRepository.count()).willReturn(numberOfAccountsInDb);

        // when
        final long numberOfAccounts = statisticsBean.getNumberOfAccounts();

        // then
        verify(accountRepository).count();
        assertThat(numberOfAccounts).isEqualTo(numberOfAccountsInDb);
    }
}
