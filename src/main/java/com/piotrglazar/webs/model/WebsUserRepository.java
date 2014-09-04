package com.piotrglazar.webs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsUserRepository extends JpaRepository<WebsUser, Long> {

    WebsUser findByUsername(String username);

    WebsUser findByEmail(String email);

    WebsUser findByAccountsId(Long accountId);
}
