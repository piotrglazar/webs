package com.piotrglazar.webs.business;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.mvc.TransferForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyTransferParamsFactory {

    private final AccountRepository accountRepository;

    private final UserProvider userProvider;

    private final MoneyAmountBuilder amountBuilder;

    @Autowired
    public MoneyTransferParamsFactory(AccountRepository accountRepository, UserProvider userProvider, MoneyAmountBuilder amountBuilder) {
        this.accountRepository = accountRepository;
        this.userProvider = userProvider;
        this.amountBuilder = amountBuilder;
    }

    public MoneyTransferParams create(final String username, final TransferForm transferForm) {
//        final long rawAmount = transferForm.getIntegralPart() * 100 + transferForm.getFractionalPart();
//        final BigDecimal amount = new BigDecimal(rawAmount).movePointLeft(2);
        final BigDecimal amount = amountBuilder.fromIntegralAndFractionalParts(transferForm.getIntegralPart(),
                transferForm.getFractionalPart());
        final Long toAccount = accountRepository.findByNumber(transferForm.getAccountNumber()).getId();

        final String email = userProvider.getUserByUsername(username).getEmail();

        return new MoneyTransferParams(username, email, transferForm.getAccountId(), toAccount, amount);
    }
}
