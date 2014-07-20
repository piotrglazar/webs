package com.piotrglazar.webs.util;

import com.piotrglazar.webs.model.AccountRepository;
import com.piotrglazar.webs.model.WebsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "webs:name=DatabaseStatistics")
public class DatabaseStatisticsBean {

    private final WebsUserRepository websUserRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public DatabaseStatisticsBean(final WebsUserRepository websUserRepository, final AccountRepository accountRepository) {
        this.websUserRepository = websUserRepository;
        this.accountRepository = accountRepository;
    }

    @ManagedOperation
    public long getNumberOfUsers() {
        return websUserRepository.count();
    }

    @ManagedOperation
    public long getNumberOfAccounts() {
        return accountRepository.count();
    }
}
