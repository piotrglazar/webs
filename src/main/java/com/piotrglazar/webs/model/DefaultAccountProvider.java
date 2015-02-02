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
import com.piotrglazar.webs.dto.SubaccountDto;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.model.entities.SavingsAccount;
import com.piotrglazar.webs.model.entities.Subaccount;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.AccountRepository;
import com.piotrglazar.webs.util.MoreCollectors;
import com.piotrglazar.webs.util.OperationLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
class DefaultAccountProvider implements AccountProvider {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    public Optional<SavingsAccountDto> getUserSavingsAccount(final String username, final long accountId) {
        final Optional<Account> account = userSavingsAccount(username, accountId);

        return account.map(rawAccount -> Optional.of(new SavingsAccountDto((SavingsAccount) rawAccount))).orElse(Optional.empty());
    }

    @Override
    public Optional<AccountDto> getAccount(final String accountNumber) {
        return Optional.ofNullable(accountRepository.findByNumber(accountNumber))
                .map(AccountDto::new)
                .map(Optional::of)
                .orElse(Optional.empty());
    }

    @Override
    @OperationLogging(operation = "newAccount")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String newAccount(final String username, final AccountType type, final Currency currency) {
        if (type == AccountType.SAVINGS) {
            return createSavingsAccount(username, currency);
        } else {
            throw new WebsRuntimeException(String.format("Unknown account type %s", type));
        }
    }

    @Override
    @OperationLogging(operation = "newSubaccount")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void newSubaccount(final String username, final long accountId, final BigDecimal subaccountBalance,
                              final String subaccountName) {
        final Optional<Account> account = userSavingsAccount(username, accountId);

        if (account.isPresent()) {
            Subaccount subaccount = new Subaccount(subaccountName, subaccountBalance);
            addSubaccount(account.get(), subaccount);
        } else {
            LOG.error("Account with id {} not found for user {}", accountId, username);
            throw new WebsAccountNotFoundException(accountId);
        }
    }

    @Override
    public Optional<SubaccountDto> getSubaccount(final String username, final long accountId, final String subaccountName) {
        final Optional<Account> accountOptional = userSavingsAccount(username, accountId);

        if (accountOptional.isPresent()) {
            final Account account = accountOptional.get();
            final Optional<Subaccount> subaccountOpt =
                    account.getSubaccounts().stream().filter(subaccount -> subaccount.getName().equals(subaccountName)).findFirst();

            return subaccountOpt
                    .map(subaccount -> Optional.of(new SubaccountDto(subaccount.getName(), subaccount.getBalance(), account.getCurrency())))
                    .orElse(Optional.empty());
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteSubaccount(final String username, final long accountId, final String subaccountName) {
        final Optional<Account> accountOptional = userSavingsAccount(username, accountId);

        if (accountOptional.isPresent()) {
            final Account account = accountOptional.get();
            final Optional<Subaccount> subaccountOpt = account.getSubaccounts().stream()
                    .filter(subaccount -> subaccount.getName().equals(subaccountName))
                    .findFirst();

            subaccountOpt
                    .map(subaccount -> {
                        account.getSubaccounts().remove(subaccount);
                        return account.addBalance(subaccount.getBalance());
                    })
                    .orElseThrow(() -> new WebsSubaccountNotFoundException(account.getNumber(), subaccountName));
            accountRepository.saveAndFlush(account);
        } else {
            LOG.error("Account with id {} not found for user {}", accountId, username);
            throw new WebsAccountNotFoundException(accountId);
        }
    }

    private void addSubaccount(final Account account, final Subaccount newSubaccount) {
        if (existsSubaccountWithTheSameName(account, newSubaccount.getName())) {
            throw new DuplicateSubaccountNameException(account.getNumber(), newSubaccount.getName());
        }
        if (!existsRequiredAmountOnAccount(account, newSubaccount)) {
            throw new InsufficientFundsException(account.getNumber(), newSubaccount.getBalance());
        }
        account.addSubaccount(newSubaccount);
        account.subtractBalance(newSubaccount.getBalance());
        accountRepository.saveAndFlush(account);
    }

    private Optional<Account> userSavingsAccount(String username, long accountId) {
        final List<Account> accounts = accountRepository.findByUsername(username);
        return getUserSavingsAccount(accounts, accountId);
    }

    private boolean existsRequiredAmountOnAccount(final Account account, final Subaccount newSubaccount) {
        return account.getBalance().compareTo(newSubaccount.getBalance()) >= 0;
    }

    private boolean existsSubaccountWithTheSameName(final Account account, final String subaccountName) {
        return account.getSubaccounts().stream().anyMatch(subaccount -> subaccount.getName().equals(subaccountName));
    }

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

    private Optional<Account> getUserSavingsAccount(final List<Account> accounts, final long accountId) {
        return accounts
                .stream()
                .filter(account -> account.getId().equals(accountId))
                .filter(account -> account instanceof SavingsAccount)
                .findAny();
    }
}
