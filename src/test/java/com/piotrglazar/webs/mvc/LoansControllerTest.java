package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.LoanProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.LoanDto;
import com.piotrglazar.webs.mvc.controllers.LoansController;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import com.piotrglazar.webs.util.LoanOption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoansControllerTest {

    @Mock
    private LoggedInUserProvider userProvider;

    @Mock
    private LoanProvider loanProvider;

    @Mock
    private AccountProvider accountProvider;

    @InjectMocks
    private LoansController controller;

    @Test
    public void shouldShowAllActiveLoans() {
        // given
        final Model model = mock(Model.class);
        final List<LoanDto> activeLoans = loans();
        given(userProvider.getLoggedInUserUsername()).willReturn("user");
        given(loanProvider.getAllActiveLoans("user")).willReturn(activeLoans);

        // when
        final String loansPage = controller.getActiveLoans(model);

        // then
        verify(model).addAttribute("loans", activeLoans);
        assertThat(loansPage).isEqualTo("loans");
    }

    @Test
    public void shouldShowAllArchiveLoans() {
        // given
        final Model model = mock(Model.class);
        final List<LoanDto> archiveLoans = loans();
        given(userProvider.getLoggedInUserUsername()).willReturn("user");
        given(loanProvider.getAllArchiveLoans("user")).willReturn(archiveLoans);

        // when
        final String archiveLoansPage = controller.getArchiveLoans(model);

        // then
        verify(model).addAttribute("archiveLoans", archiveLoans);
        assertThat(archiveLoansPage).isEqualTo("archiveLoans");
    }

    @Test
    public void shouldShowAllLoanOptionsAndUserAccounts() {
        // given
        final LoanCreationForm form = mock(LoanCreationForm.class);
        final List<AccountDto> accounts = accounts();
        given(userProvider.getLoggedInUserUsername()).willReturn("user");
        given(accountProvider.getUserAccounts("user")).willReturn(accounts);

        // when
        final ModelAndView modelAndView = controller.newLoan(form);

        // then
        assertThat(modelAndView.getModel())
                .containsEntry("loanCreationForm", form)
                .containsEntry("allAccounts", accounts)
                .containsEntry("allLoanOptions", LoanOption.values())
                .containsEntry(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "loans")
                .hasSize(4);
    }

    @Test
    public void shouldShowChosenFieldsWhenThereAreValidationErrors() {
        // given
        final LoanCreationForm form = mock(LoanCreationForm.class);
        final Model model = mock(Model.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasErrors()).willReturn(true);
        given(userProvider.getLoggedInUserUsername()).willReturn("user");

        // when
        final String loanPage = controller.newLoan(model, form, bindingResult);

        // then
        verify(model).addAttribute(eq("allAccounts"), anyListOf(AccountDto.class));
        verify(model).addAttribute("allLoanOptions", LoanOption.values());
        verify(accountProvider).getUserAccounts("user");
        // no redirect
        assertThat(loanPage).isEqualTo("newLoan");
    }

    @Test
    public void shouldCreateLoanAndThenRedirectToActiveLoans() {
        // given
        final LoanCreationForm form = mock(LoanCreationForm.class);
        final Model model = mock(Model.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        given(userProvider.getLoggedInUserUsername()).willReturn("user");

        // when
        final String view = controller.newLoan(model, form, bindingResult);

        // then
        assertThat(view).isEqualTo("redirect:/loans");
        verify(loanProvider).createLoan(form, "user");
    }

    @Test
    public void shouldDelegatePostponeLoanActionToAnotherComponent() {
        // given
        final long loanId = 123;
        given(userProvider.getLoggedInUserUsername()).willReturn("user");

        // when
        final String view = controller.postponeLoan(loanId);

        // then
        assertThat(view).isEqualTo("redirect:/loans");
        verify(loanProvider).postponeLoanIfUserIsItsOwner("user", loanId);
    }

    private List<LoanDto> loans() {
        return Lists.newArrayList(mock(LoanDto.class), mock(LoanDto.class));
    }

    private List<AccountDto> accounts() {
        return Lists.newArrayList(mock(AccountDto.class), mock(AccountDto.class));
    }
}
