package com.piotrglazar.webs.business.interest;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.DatabaseTestConfiguration;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class InterestAccruerContextTest extends AbstractContextTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InterestAccruer interestAccruer;

    @Test
    public void shouldAccrueInterest() {
        // given
        restoreOriginalBalances();

        // when
        interestAccruer.accrueInterest();

        // then
        final List<Account> accounts = accountRepository.findAll();
        accounts.sort((a1, a2) -> a1.getNumber().compareTo(a2.getNumber()));
        final List<BigDecimal> balances = accounts.stream().map(Account::getBalance).collect(Collectors.toList());
        assertThat(balances).containsOnly(new BigDecimal("1000.15"), new BigDecimal("4000.49"), new BigDecimal("1000.15"),
                new BigDecimal("4000.49"));
    }

    private void restoreOriginalBalances() {
        final List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(4);
        // sort by number
        accounts.sort((a1, a2) -> a1.getNumber().compareTo(a2.getNumber()));
        for (int i = 0; i < accounts.size(); ++i) {
            accounts.get(i).setBalance(DatabaseTestConfiguration.ACCOUNT_BALANCES.get(i % 2));
        }

        accountRepository.save(accounts);
    }
}
