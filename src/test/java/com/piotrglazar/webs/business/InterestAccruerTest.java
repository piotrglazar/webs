package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.InterestCalculator;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InterestAccruerTest {

    private SavingsAccount account = new SavingsAccount("account no", Currency.GBP, BigDecimal.ONE, BigDecimal.ZERO);

    @Mock
    private AccountRepository repository;

    @Mock
    private InterestCalculator calculator;

    private List<InterestCalculator> calculators;

    private InterestAccruer interestAccruer;

    @Before
    public void setUp() {
        calculators = Lists.newArrayList(calculator);
        interestAccruer = new InterestAccruer(repository, calculators);
    }

    @Test
    public void shouldBuildCalculatorsCache() {
        // given
        given(calculator.supports(AccountType.SAVINGS)).willReturn(true);

        // when
        interestAccruer.buildInterestCalculators();

        // then
        verify(calculator).supports(AccountType.SAVINGS);
    }

    @Test
    public void shouldAccrueInterest() {
        // given
        final ArrayList<Account> accounts = Lists.newArrayList(account);
        given(repository.findAll()).willReturn(accounts);
        given(calculator.supports(AccountType.SAVINGS)).willReturn(true);
        given(calculator.calculateInterest(account)).willReturn(BigDecimal.TEN);
        interestAccruer.buildInterestCalculators();

        // when
        interestAccruer.accrueInterest();

        // then
        verify(repository).save(accounts);
        verify(calculator).calculateInterest(account);
        assertThat(account.getBalance()).isEqualTo(new BigDecimal(11));
    }
}
