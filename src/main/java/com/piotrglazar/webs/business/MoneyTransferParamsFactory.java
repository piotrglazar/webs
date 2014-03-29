package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.model.WebsUserRepository;
import com.piotrglazar.webs.mvc.TransferForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyTransferParamsFactory {

    private final AccountRepository accountRepository;

    private final WebsUserRepository userRepository;

    @Autowired
    public MoneyTransferParamsFactory(final AccountRepository accountRepository, final WebsUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public MoneyTransferParams create(final String username, final TransferForm transferForm) {
        final long rawAmount = transferForm.getIntegralPart() * 100 + transferForm.getFractionalPart();
        final BigDecimal amount = new BigDecimal(rawAmount).movePointLeft(2);
        final Long toAccount = accountRepository.findByNumber(transferForm.getAccountNumber()).getId();

        final String email = userRepository.findByUsername(username).getEmail();

        return new MoneyTransferParams(username, email, transferForm.getAccountId(), toAccount, amount);
    }
}
