package com.piotrglazar.webs.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.loans.DefaultLoanBusinessLogic;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.LoanDto;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.entities.LoanBuilder;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.model.repositories.LoanRepository;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLoanProviderTest {

    @Mock
    private Supplier<LocalDateTime> dateTimeSupplier;

    @Mock
    private LoanFactory loanFactory;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserProvider userProvider;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DefaultLoanBusinessLogic loanBusinessLogic;

    @InjectMocks
    private DefaultLoanProvider loanProvider;

    @Test
    public void shouldConvertActiveLoanToLoanDto() {
        // given
        final Account account = mock(Account.class);
        given(account.getId()).willReturn(1L);
        given(account.getNumber()).willReturn("account no");
        final Loan loan = new LoanBuilder()
                .account(account)
                .amountLoaned(new BigDecimal("15"))
                .amountRemaining(new BigDecimal("5"))
                .currency(Currency.GBP)
                .postpones(Sets.newHashSet(LocalDate.of(2014, 8, 1)))
                .weeks((byte) 4)
                .weeksRemaining((byte) 1)
                .build();
        given(loanRepository.findByUsername("user")).willReturn(Lists.newArrayList(loan));
        given(loanBusinessLogic.canPostpone(loan)).willReturn(true);

        // when
        final List<LoanDto> loans = loanProvider.getAllActiveLoans("user");

        // then
        assertThat(loans).hasSize(1);
        final LoanDto dto = loans.get(0);
        assertThat(dto.getAccountId()).isEqualTo(1);
        assertThat(dto.getAccountNumber()).isEqualTo("account no");
        assertThat(dto.getAmountLoaned()).isEqualByComparingTo("15");
        assertThat(dto.getAmountRemaining()).isEqualByComparingTo("5");
        assertThat(dto.getCurrency()).isEqualTo(Currency.GBP);
        assertThat(dto.getPostpones()).isEqualTo(Sets.newHashSet(LocalDate.of(2014, 8, 1)));
        assertThat(dto.getWeeks()).isEqualTo((byte) 4);
        assertThat(dto.getWeeksRemaining()).isEqualTo((byte) 1);
        assertThat(dto.getCanPostpone()).isTrue();
    }

    @Test
    public void shouldConvertArchiveLoanToLoanDto() {
        // given
        final Account account = mock(Account.class);
        given(account.getId()).willReturn(1L);
        given(account.getNumber()).willReturn("account no");
        final Loan loan = new LoanBuilder()
                .account(account)
                .amountLoaned(new BigDecimal("15"))
                .amountRemaining(new BigDecimal("5"))
                .currency(Currency.GBP)
                .postpones(Sets.newHashSet(LocalDate.of(2014, 8, 1)))
                .weeks((byte) 4)
                .weeksRemaining((byte) 0)
                .build();
        given(loanRepository.findByUsername("user")).willReturn(Lists.newArrayList(loan));
        given(loanBusinessLogic.canPostpone(loan)).willReturn(true);

        // when
        final List<LoanDto> loans = loanProvider.getAllArchiveLoans("user");

        // then
        assertThat(loans).hasSize(1);
        final LoanDto dto = loans.get(0);
        assertThat(dto.getAccountId()).isEqualTo(1);
        assertThat(dto.getAccountNumber()).isEqualTo("account no");
        assertThat(dto.getAmountLoaned()).isEqualByComparingTo("15");
        assertThat(dto.getAmountRemaining()).isEqualByComparingTo("5");
        assertThat(dto.getCurrency()).isEqualTo(Currency.GBP);
        assertThat(dto.getPostpones()).isEqualTo(Sets.newHashSet(LocalDate.of(2014, 8, 1)));
        assertThat(dto.getWeeks()).isEqualTo((byte) 4);
        assertThat(dto.getWeeksRemaining()).isEqualTo((byte) 0);
        assertThat(dto.getCanPostpone()).isFalse();
    }

    @Test
    public void shouldCreateLoan() {
        // given
        final LoanCreationForm form = mock(LoanCreationForm.class);
        final String username = "user";
        final WebsUser user = new WebsUserBuilder().username(username).build();
        final Account account = mock(Account.class);
        final Loan loan = new LoanBuilder().build();
        given(userProvider.getUserByUsername(username)).willReturn(user);
        given(accountRepository.findOne(anyLong())).willReturn(account);
        given(loanFactory.create(form, account)).willReturn(loan);

        // when
        loanProvider.createLoan(form, username);

        // then
        verify(loanFactory).create(form, account);
        verify(userProvider).update(user);
        assertThat(user.getLoans()).contains(loan);
    }

    @Test
    public void shouldNotPostponeLoanIfUserIsNotItsOwner() {
        // given
        final String username = "user";
        final long loanId = 123;
        given(loanRepository.findByUsername(username)).willReturn(loans());
        given(loanRepository.findOne(loanId)).willReturn(new LoanBuilder().id(3).build());

        // when
        final boolean postponed = loanProvider.postponeLoanIfUserIsItsOwner(username, loanId);

        // then
        assertThat(postponed).isFalse();
    }

    @Test
    public void shouldPostponeUsersLoan() {
        // given
        final String username = "user";
        final long loanId = 123;
        final List<Loan> loans = loans();
        final LocalDateTime postponeDate = LocalDateTime.of(2014, 8, 2, 0, 0);
        given(loanRepository.findByUsername(username)).willReturn(loans);
        given(loanRepository.findOne(loanId)).willReturn(loans.get(0));
        given(dateTimeSupplier.get()).willReturn(postponeDate);

        // when
        final boolean postponed = loanProvider.postponeLoanIfUserIsItsOwner(username, loanId);

        // then
        assertThat(postponed).isTrue();
        assertThat(loans.get(0).getPostpones()).containsOnly(postponeDate.toLocalDate());
    }

    private List<Loan> loans() {
        final List<Loan> loans = Lists.newArrayList();

        loans.add(new LoanBuilder().id(1).build());
        loans.add(new LoanBuilder().id(2).build());

        return loans;
    }
}
