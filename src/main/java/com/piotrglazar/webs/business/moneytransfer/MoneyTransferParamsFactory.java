package com.piotrglazar.webs.business.moneytransfer;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.mvc.forms.TransferForm;
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
        final BigDecimal amount = amountBuilder.fromIntegralAndFractionalParts(transferForm.getIntegralPart(),
                transferForm.getFractionalPart());
        final Account toAccount = accountRepository.findByNumber(transferForm.getAccountNumber());

        final String email = userProvider.getUserByUsername(username).getEmail();

        final WebsUser receivingUser = userProvider.findUserByAccountId(toAccount.getId());

        return new MoneyTransferParamsBuilder()
                    .username(username)
                    .email(email)
                    .fromAccount(transferForm.getAccountId())
                    .toAccount(toAccount.getId())
                    .amount(amount)
                    .receivingUserId(receivingUser.getId())
                    .receivingUsername(receivingUser.getUsername())
                    .currency(toAccount.getCurrency())
                    .build();
    }
}
