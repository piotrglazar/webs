package com.piotrglazar.webs.business;

import com.piotrglazar.webs.LoanBusinessLogic;
import com.piotrglazar.webs.model.entities.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.function.Supplier;

@Component
public class DefaultLoanBusinessLogic implements LoanBusinessLogic {

    public static final int MAX_POSTPONES = 3;

    private final Supplier<LocalDateTime> dateSupplier;
    private final AmountConverter amountConverter;

    @Autowired
    public DefaultLoanBusinessLogic(final Supplier<LocalDateTime> dateSupplier, final AmountConverter amountConverter) {
        this.dateSupplier = dateSupplier;
        this.amountConverter = amountConverter;
    }

    public boolean canPostpone(final Loan loan) {
        return !hasExceededMaximumNumberOfPostpones(loan) && !hasAlreadyPostponedThisWeek(loan);
    }

    public BigDecimal amountToPay(final Loan loan) {
        if (hasAlreadyPostponedThisWeek(loan)) {
            return BigDecimal.ZERO;
        }

        if (loan.getWeeksRemaining() == 1) {
            // last week, just pay the rest
            return loan.getAmountRemaining();
        } else {
            return loan.getAmountLoaned()
                    .divide(new BigDecimal(loan.getWeeks()), amountConverter.getScale(), amountConverter.getRoundingMode());
        }
    }

    private boolean hasAlreadyPostponedThisWeek(final Loan loan) {
        final LocalDateTime currentDate = dateSupplier.get();

        return loan.getPostpones().stream()
                .map(postponeDate -> currentDate.getYear() == postponeDate.getYear() &&
                        currentDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == postponeDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                .reduce(false, (a, b) -> a || b);
    }

    private boolean hasExceededMaximumNumberOfPostpones(final Loan loan) {
        return loan.getPostpones().size() > MAX_POSTPONES;
    }
}
