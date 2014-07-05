package com.piotrglazar.webs.business;

import com.google.common.base.Preconditions;
import com.piotrglazar.webs.InterestCalculator;
import com.piotrglazar.webs.InterestCalculationStrategy;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountType;
import com.piotrglazar.webs.model.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class SavingsAccountInterestCalculator implements InterestCalculator {

    private final InterestCalculationStrategy strategy;

    @Autowired
    public SavingsAccountInterestCalculator(InterestCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean supports(final AccountType accountType) {
        return AccountType.SAVINGS.equals(accountType);
    }

    @Override
    public BigDecimal calculateInterest(final Account account) {
        Preconditions.checkArgument(supports(account.accountType()), "Only savingsAccount is supported, not " + account.accountType());

        final BigDecimal balance = account.getBalance();
        final BigDecimal interest = ((SavingsAccount) account).getInterest();
        return strategy.interest(balance, interest);
    }
}
