package com.piotrglazar.webs.business;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.entities.LoanBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class LoanBusinessLogicTest {

    @Mock
    private AmountConverter amountConverter;

    @Mock
    private Supplier<LocalDateTime> dateTimeSupplier;

    @InjectMocks
    private DefaultLoanBusinessLogic loanBusinessLogic;

    @Test
    public void shouldAllowToPostponeNewLoan() {
        // given
        final Loan loan = new LoanBuilder().build();
        given(dateTimeSupplier.get()).willReturn(LocalDateTime.of(2014, 8, 1, 0, 0));

        // when
        final boolean canPostpone = loanBusinessLogic.canPostpone(loan);

        // then
        assertThat(canPostpone).isTrue();
    }

    @Test
    public void shouldNotAllowToPostponeLoanWhichWasPostponedMaximumTimes() {
        // given
        final Set<LocalDate> postpones = postpones(DefaultLoanBusinessLogic.MAX_POSTPONES + 1);
        final Loan loan = new LoanBuilder().postpones(postpones).build();

        // when
        final boolean canPostpone = loanBusinessLogic.canPostpone(loan);

        // then
        assertThat(canPostpone).isFalse();
    }

    @Test
    public void shouldNotAllowToPostponeLoanWhichWasPostponedThisWeek() {
        // given
        final Set<LocalDate> postpones = Sets.newHashSet(LocalDate.of(2014, 8, 1));
        final Loan loan = new LoanBuilder().postpones(postpones).build();
        given(dateTimeSupplier.get()).willReturn(LocalDateTime.of(2014, 8, 2, 0, 0));

        // when
        final boolean canPostpone = loanBusinessLogic.canPostpone(loan);

        // then
        assertThat(canPostpone).isFalse();
    }

    @Test
    public void shouldAllowToPostponeLoanWhichWasPostponedPreviousWeek() {
        // given
        final Set<LocalDate> postpones = Sets.newHashSet(LocalDate.of(2014, 8, 3));
        final Loan loan = new LoanBuilder().postpones(postpones).build();
        given(dateTimeSupplier.get()).willReturn(LocalDateTime.of(2014, 8, 4, 0, 0));

        // when
        final boolean canPostpone = loanBusinessLogic.canPostpone(loan);

        // then
        assertThat(canPostpone).isTrue();
    }

    @Test
    public void shouldAmountToBePaidBeEqualTo0ForPostponedLoan() {
        // given
        final Set<LocalDate> postpones = Sets.newHashSet(LocalDate.of(2014, 8, 3));
        final Loan loan = new LoanBuilder().postpones(postpones).build();
        given(dateTimeSupplier.get()).willReturn(LocalDateTime.of(2014, 8, 3, 0, 0));

        // when
        final BigDecimal amountToPay = loanBusinessLogic.amountToPay(loan);

        // then
        assertThat(amountToPay).isEqualByComparingTo("0");
    }

    @Test
    public void shouldAmountToBePaidBeEqualToRemainingAmountForLoanWithOneWeekRemaining() {
        // given
        final Loan loan = new LoanBuilder().amountRemaining(new BigDecimal("123")).weeksRemaining((byte) 1).build();
        final LocalDateTime arbitraryDate = LocalDateTime.of(2014, 8, 3, 0, 0);
        given(dateTimeSupplier.get()).willReturn(arbitraryDate);

        // when
        final BigDecimal amountToPay = loanBusinessLogic.amountToPay(loan);

        // then
        assertThat(amountToPay).isEqualByComparingTo("123");
    }

    @Test
    public void shouldAmountToBePaidBeEqualToOneNthOfAmountLoaned() {
        // given
        final Loan loan = new LoanBuilder().weeksRemaining((byte) 4).weeks((byte) 4).amountLoaned(new BigDecimal("1000")).build();
        given(amountConverter.getScale()).willReturn(2);
        given(amountConverter.getRoundingMode()).willReturn(RoundingMode.HALF_UP);

        // when
        final BigDecimal amountToPay = loanBusinessLogic.amountToPay(loan);

        // then
        assertThat(amountToPay).isEqualByComparingTo("250.00");
    }

    private Set<LocalDate> postpones(final int maxPostpones) {
        return IntStream.range(0, maxPostpones)
                .mapToObj(i -> LocalDate.of(2014 + i, 8, 1))
                .collect(Collectors.toSet());
    }
}
