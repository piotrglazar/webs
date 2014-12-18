package com.piotrglazar.webs.business.loans;

import com.piotrglazar.webs.LoanBusinessLogic;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanRepays {

    private final LoanBusinessLogic loanBusinessLogic;
    private final LoanRepository loanRepository;

    @Autowired
    public LoanRepays(LoanBusinessLogic loanBusinessLogic, LoanRepository loanRepository) {
        this.loanBusinessLogic = loanBusinessLogic;
        this.loanRepository = loanRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void repayAllLoans() {
        final List<LoanAndPayment> loanAndPayments = loanRepository.findAll().stream()
                .map(loan -> new LoanAndPayment(loan, loanBusinessLogic.amountToPay(loan)))
                .collect(Collectors.toList());

        for (LoanAndPayment loanAndPayment : loanAndPayments) {
            final Account account = loanAndPayment.loan.getAccount();
            account.subtractBalance(loanAndPayment.payment);
            loanAndPayment.loan.payOneWeek(loanAndPayment.payment);
        }

        final List<Loan> loans = loanAndPayments.stream().map(loanAndPayment -> loanAndPayment.loan).collect(Collectors.toList());
        loanRepository.save(loans);
    }

    private static class LoanAndPayment {
        private final Loan loan;
        private final BigDecimal payment;

        private LoanAndPayment(final Loan loan, final BigDecimal payment) {
            this.loan = loan;
            this.payment = payment;
        }
    }
}
