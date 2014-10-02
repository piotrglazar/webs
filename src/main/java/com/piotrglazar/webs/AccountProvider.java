package com.piotrglazar.webs;

import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountProvider {

    List<AccountDto> getUserAccounts(String username);

    Optional<SavingsAccountDto> getUserSavingsAccount(String username, Long accountId);

    AccountDto getAccount(String accountNumber);

    String newAccount(String username, AccountType type, Currency currency);
}
