package com.piotrglazar.webs.model;

import com.piotrglazar.webs.LoanBusinessLogic;
import com.piotrglazar.webs.LoanProvider;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.dto.LoanDto;
import com.piotrglazar.webs.dto.LoanDtoBuilder;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.model.repositories.LoanRepository;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import com.piotrglazar.webs.util.MoreCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Component
class DefaultLoanProvider implements LoanProvider {

    private final LoanRepository loanRepository;
    private final LoanFactory loanFactory;
    private final AccountRepository accountRepository;
    private final UserProvider userProvider;
    private final LoanBusinessLogic loanBusinessLogic;
    private final Supplier<LocalDateTime> dateTimeSupplier;

    @Autowired
    public DefaultLoanProvider(LoanRepository loanRepository, LoanFactory loanFactory, AccountRepository accountRepository,
                               UserProvider userProvider, LoanBusinessLogic loanBusinessLogic, Supplier<LocalDateTime> dateTimeSupplier) {
        this.loanRepository = loanRepository;
        this.loanFactory = loanFactory;
        this.accountRepository = accountRepository;
        this.userProvider = userProvider;
        this.loanBusinessLogic = loanBusinessLogic;
        this.dateTimeSupplier = dateTimeSupplier;
    }

    @Override
    public List<LoanDto> getAllActiveLoans(String username) {
        return loanRepository.findByUsername(username).stream()
                .filter(loan -> loan.getWeeksRemaining() > 0)
                .map(loan -> {loan.setCanPostpone(loanBusinessLogic.canPostpone(loan)); return loan;})
                .map(LoanDtoBuilder::fromLoan)
                .collect(MoreCollectors.toImmutableList());
    }

    @Override
    public List<LoanDto> getAllArchiveLoans(String username) {
        return loanRepository.findByUsername(username).stream()
                .filter(loan -> loan.getWeeksRemaining() == 0)
                .map(LoanDtoBuilder::fromLoan)
                .collect(MoreCollectors.toImmutableList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void createLoan(final LoanCreationForm loanCreationForm, final String username) {
        final WebsUser websUser = userProvider.getUserByUsername(username);
        final Loan loan = loanFactory.create(loanCreationForm, accountRepository.findOne(loanCreationForm.getAccountId()));

        websUser.addLoan(loan);
        userProvider.update(websUser);
    }

    /*
     * Due to strange transaction behavior we have to do two operations (check whether user it the owner of loan and postponing it)
     * inside one @Transactional. Otherwise it was failing due to transaction read only state.
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public boolean postponeLoanIfUserIsItsOwner(final String username, final long loanId) {
        final Loan loan = loanRepository.findOne(loanId);
        final List<Loan> loans = loanRepository.findByUsername(username);

        if (loans.contains(loan)) {
            postpone(loan);
            return true;
        } else {
            return false;
        }
    }

    private void postpone(final Loan loan) {
        loan.getPostpones().add(dateTimeSupplier.get().toLocalDate());

        loanRepository.saveAndFlush(loan);
    }
}
