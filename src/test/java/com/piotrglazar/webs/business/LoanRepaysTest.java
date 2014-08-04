package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.Loan;
import com.piotrglazar.webs.model.LoanBuilder;
import com.piotrglazar.webs.model.LoanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoanRepaysTest {

    @Mock
    private LoanBusinessLogic loanBusinessLogic;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanRepays loanRepays;

    @Test
    public void shouldNotFailWhenThereAreNoLoans() {
        // given
        given(loanRepository.findAll()).willReturn(Collections.emptyList());

        // when
        loanRepays.repayAllLoans();

        // then
        verify(loanRepository).save(Collections.emptyList());
    }

    @Test
    public void shouldRepayLoans() {
        // given
        final List<Loan> loans = loans();
        given(loanRepository.findAll()).willReturn(loans);
        given(loanBusinessLogic.amountToPay(loans.get(0))).willReturn(new BigDecimal("100"));
        given(loanBusinessLogic.amountToPay(loans.get(1))).willReturn(new BigDecimal("200"));

        // when
        loanRepays.repayAllLoans();

        // then
        verify(loanRepository).save(loans);
        verify(loans.get(0).getAccount()).subtractBalance(new BigDecimal("100"));
        verify(loans.get(1).getAccount()).subtractBalance(new BigDecimal("200"));
        assertThat(loans.get(0).getWeeksRemaining()).isEqualTo((byte) 0);
        assertThat(loans.get(1).getWeeksRemaining()).isEqualTo((byte) 0);
        assertThat(loans.get(0).getAmountRemaining()).isEqualByComparingTo("-100");
        assertThat(loans.get(1).getAmountRemaining()).isEqualByComparingTo("-200");
    }

    private List<Loan> loans() {
        final List<Loan> loans = Lists.newLinkedList();

        loans.add(new LoanBuilder().id(1).account(mock(Account.class)).weeksRemaining((byte) 1).build());
        loans.add(new LoanBuilder().id(2).account(mock(Account.class)).weeksRemaining((byte) 1).build());

        return loans;
    }
}
