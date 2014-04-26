package com.piotrglazar.webs.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select u.accounts from WebsUser u where u.username = ?1")
    List<Account> findByUsername(String username);

    Account findByNumber(String number);
}
