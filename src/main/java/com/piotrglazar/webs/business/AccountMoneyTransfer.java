package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.util.ErrorGatherer;
import com.piotrglazar.webs.util.OperationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class AccountMoneyTransfer {

    private final AccountRepository accountRepository;

    private final BusinessMailSender mailSender;

    private final MoneyTransferValidatorChain validatorChain;

    @Autowired
    public AccountMoneyTransfer(final AccountRepository accountRepository, final BusinessMailSender mailSender,
                                final MoneyTransferValidatorChain validatorChain) {
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
        this.validatorChain = validatorChain;
    }

    @OperationLogging(operation = "transferMoney")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void transferMoney(final MoneyTransferParams moneyTransferParams) {
        doTransferMoney(moneyTransferParams);

        mailSender.sendMoneyTransferMessage(moneyTransferParams);
    }

    private void doTransferMoney(final MoneyTransferParams moneyTransferParams) {
        final Account fromAccount = accountRepository.findOne(moneyTransferParams.getFromAccount());
        final Account toAccount = accountRepository.findOne(moneyTransferParams.getToAccount());
        final ErrorGatherer errors = new ErrorGatherer();

        validatorChain.validateAll(new MoneyTransferDetails(toAccount, fromAccount, moneyTransferParams), errors);

        if (errors.isEmpty()) {
            final BigDecimal amount = moneyTransferParams.getAmount();

            fromAccount.subtractBalance(amount);
            toAccount.addBalance(amount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } else {
            throw new MoneyTransferException(errors);
        }
    }
}
