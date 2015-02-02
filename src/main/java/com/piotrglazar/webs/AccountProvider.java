package com.piotrglazar.webs;

import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.dto.SubaccountDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountProvider {

    List<AccountDto> getUserAccounts(String username);

    Optional<SavingsAccountDto> getUserSavingsAccount(String username, long accountId);

    Optional<AccountDto> getAccount(String accountNumber);

    String newAccount(String username, AccountType type, Currency currency);

    void newSubaccount(String username, long accountId, BigDecimal subaccountAmount, String subaccountName);

    Optional<SubaccountDto> getSubaccount(String username, long accountId, String subaccountName);

    void deleteSubaccount(String username, long accountId, String subaccountName);
}
