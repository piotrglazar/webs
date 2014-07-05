package com.piotrglazar.webs.business;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountRepository;
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
        // when
        interestAccruer.accrueInterest();

        // then
        final List<Account> accounts = accountRepository.findAll();
        accounts.sort((a1, a2) -> a1.getNumber().compareTo(a2.getNumber()));
        final List<BigDecimal> balances = accounts.stream().map(Account::getBalance).collect(Collectors.toList());
        assertThat(balances).containsExactly(new BigDecimal("1000.15"), new BigDecimal("4000.49"), new BigDecimal("1000.15"),
                new BigDecimal("4000.49"));
    }
}
