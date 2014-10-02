package com.piotrglazar.webs.model;

import com.piotrglazar.webs.LoanCalculator;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.entities.LoanBuilder;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoanFactory {

    private final LoanCalculator loanCalculator;

    @Autowired
    public LoanFactory(final LoanCalculator loanCalculator) {
        this.loanCalculator = loanCalculator;
    }

    public Loan create(LoanCreationForm form, Account account) {
        final BigDecimal amountToBePaid = loanCalculator.calculateLoan(form.getAmountLoaned(), form.getLoanOption());

        return new LoanBuilder()
                .amountLoaned(form.getAmountLoaned())
                .amountRemaining(amountToBePaid.add(form.getAmountLoaned()))
                .weeks(form.getLoanOption().getWeeks())
                .weeksRemaining(form.getLoanOption().getWeeks())
                .account(account)
                .currency(account.getCurrency())
                .build();
    }
}
