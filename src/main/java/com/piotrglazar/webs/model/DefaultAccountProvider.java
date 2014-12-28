package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.UniqueIdGenerator;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.WebsRuntimeException;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.AccountDtoFactory;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.util.MoreCollectors;
import com.piotrglazar.webs.util.OperationLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
class DefaultAccountProvider implements AccountProvider {

    private final AccountRepository accountRepository;
    private final UserProvider userProvider;
    private final UniqueIdGenerator generator;
    private final AccountDtoFactory factory;

    @Value("#{businessProperties['savingsaccount.interest']?:0}")
    private BigDecimal interest;

    @Autowired
    public DefaultAccountProvider(AccountRepository accountRepository, UserProvider userProvider, UniqueIdGenerator generator,
                                  AccountDtoFactory factory) {
        this.accountRepository = accountRepository;
        this.userProvider = userProvider;
        this.generator = generator;
        this.factory = factory;
    }

    @Override
    public List<AccountDto> getUserAccounts(final String username) {
        final List<Account> accounts = accountRepository.findByUsername(username);

        return accounts.stream().map(factory::dto).collect(MoreCollectors.toImmutableList());
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

    @Override
    public AccountDto getAccount(final String accountNumber) {
        return Optional.ofNullable(accountRepository.findByNumber(accountNumber)).map(AccountDto::new).orElse(null);
    }

    @Override
    @OperationLogging(operation = "newAccount")
    public String newAccount(final String username, final AccountType type, final Currency currency) {
        if (type == AccountType.SAVINGS) {
            return createSavingsAccount(username, currency);
        } else {
            throw new WebsRuntimeException(String.format("Unknown account type %s", type));
        }
    }

    @Transactional
    private String createSavingsAccount(final String username, final Currency currency) {
        final WebsUser user = userProvider.getUserByUsername(username);

        final SavingsAccount savingsAccount = SavingsAccount.builder().number(accountNumber()).currency(currency).balance(BigDecimal.ZERO)
                .interest(interest).build();

        user.getAccounts().add(savingsAccount);
        userProvider.update(user);

        return savingsAccount.getNumber();
    }

    private String accountNumber() {
        String accountNumber = generator.generate();

        while (accountRepository.findByNumber(accountNumber) != null) {
            accountNumber = generator.generate();
        }

        return accountNumber;
    }

    private Optional<Account> getUserSavingsAccount(final List<Account> accounts, final Long accountId) {
        return accounts
                .stream()
                .filter(account -> account.getId().equals(accountId))
                .filter(account -> account instanceof SavingsAccount)
                .findAny();
    }
}
