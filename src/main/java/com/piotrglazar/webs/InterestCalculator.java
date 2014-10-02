package com.piotrglazar.webs;

import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.model.entities.Account;

import java.math.BigDecimal;

public interface InterestCalculator {

    boolean supports(AccountType accountType);

    BigDecimal calculateInterest(Account account);
}
