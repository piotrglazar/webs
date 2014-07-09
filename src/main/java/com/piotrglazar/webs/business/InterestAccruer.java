package com.piotrglazar.webs.business;

import com.piotrglazar.webs.InterestCalculator;
import com.piotrglazar.webs.model.Account;
import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.model.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InterestAccruer {

    private final AccountRepository accountRepository;
    private final Map<AccountType, InterestCalculator> interestCalculatorsCache;
    private final List<InterestCalculator> interestCalculators;

    @Autowired
    public InterestAccruer(AccountRepository accountRepository, List<InterestCalculator> interestCalculators) {
        this.accountRepository = accountRepository;
        this.interestCalculatorsCache = new EnumMap<>(AccountType.class);
        this.interestCalculators = interestCalculators;
    }

    @PostConstruct
    public void buildInterestCalculators() {
        // we take first found calculator for given account type
        Arrays.asList(AccountType.values()).stream()
                .forEach(accountType ->
                        interestCalculatorsCache.put(accountType,
                                interestCalculators.stream().filter(calc -> calc.supports(accountType)).findFirst().get()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void accrueInterest() {
        final List<Account> accounts = accountRepository.findAll();
        final List<Account> processedAccounts = accounts.stream()
                .map(a -> a.addBalance(interestCalculatorsCache.get(a.accountType()).calculateInterest(a)))
                .collect(Collectors.toList());
        accountRepository.save(processedAccounts);
    }
}
