package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class DefaultAccountProvider implements AccountProvider {

    private AccountRepository accountRepository;

    @Autowired
    public DefaultAccountProvider(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> getUserAccounts(final String username) {
        final List<Account> accounts = accountRepository.findByUsername(username);

        return accounts.stream().map(AccountDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<SavingsAccountDto> getUserSavingsAccount(final String username, final Long accountId) {
        final List<Account> accounts = accountRepository.findByUsername(username);
        final Optional<Account> account = getUserSavingsAccount(accounts, accountId);
        if (account.isPresent()) {
            return Optional.of(new SavingsAccountDto((SavingsAccount) account.get()));
        } else {
            return Optional.empty();
        }
    }

    private Optional<Account> getUserSavingsAccount(final List<Account> accounts, final Long accountId) {
        return accounts
                .stream()
                .filter(account -> account.getId().equals(accountId))
                .filter(account -> account instanceof SavingsAccount)
                .findAny();
    }
}
