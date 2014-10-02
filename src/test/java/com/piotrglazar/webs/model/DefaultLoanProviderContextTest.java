package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.LoanProvider;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.repositories.LoanRepository;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import com.piotrglazar.webs.util.LoanOption;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultLoanProviderContextTest extends AbstractContextTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanProvider loanProvider;

    @Test
    public void shouldCreateLoan() {
        // given
        final LoanCreationForm form = loanCreationForm();

        // when
        loanProvider.createLoan(form, Settings.USERNAME);

        // then
        assertThat(loanRepository.findAll()).hasSize(1);

        // cleanup
        loanRepository.deleteAll();
    }

    @Test
    public void shouldPostponeLoanIfUserIsItsOwner() {
        // given
        final LoanCreationForm form = loanCreationForm();
        loanProvider.createLoan(form, Settings.USERNAME);
        final Long loanId = loanId();

        // when
        loanProvider.postponeLoanIfUserIsItsOwner(Settings.USERNAME, loanId);

        // then
        final Loan loan = loan();
        assertThat(loan.getPostpones()).hasSize(1);

        // cleanup
        loanRepository.deleteAll();
    }

    @Test
    public void shouldNotPostponeLoanIfUserIsNotOwner() {
        // given
        final LoanCreationForm form = loanCreationForm();
        loanProvider.createLoan(form, Settings.USERNAME);
        final Long loanId = loanId();

        // when
        loanProvider.postponeLoanIfUserIsItsOwner(Settings.USERNAME2, loanId);

        // then
        final Loan loan = loan();
        assertThat(loan.getPostpones()).hasSize(0);

        // cleanup
        loanRepository.deleteAll();
    }

    private Long loanId() {
        return loan().getId();
    }

    private Loan loan() {
        final List<Loan> allLoans = loanRepository.findAll();
        assertThat(allLoans).hasSize(1);
        return allLoans.get(0);
    }

    private LoanCreationForm loanCreationForm() {
        final LoanCreationForm form = new LoanCreationForm();

        form.setAccountId(1L);
        form.setLoanOption(LoanOption.LONG);
        form.setAmountLoaned(new BigDecimal("1000"));

        return form;
    }
}
