package com.piotrglazar.webs;

import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountType;

import java.math.BigDecimal;

public interface InterestCalculator {

    boolean supports(AccountType accountType);

    BigDecimal calculateInterest(Account account);
}
