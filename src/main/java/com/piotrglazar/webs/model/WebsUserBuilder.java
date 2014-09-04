package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;

import java.time.LocalDateTime;
import java.util.Set;

public class WebsUserBuilder {
    private Long id;
    private String username = "";
    private String password = "";
    private String email = "";
    private Set<String> roles = Sets.newHashSet("USER");
    private Set<Account> accounts = Sets.newHashSet();
    private WebsUserDetails details = new WebsUserDetails(LocalDateTime.of(2014, 9, 4, 0, 0), new Address("Warsaw", "Poland"));
    private Set<Loan> loans = Sets.newHashSet();

    public WebsUserBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public WebsUserBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public WebsUserBuilder roles(final Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public WebsUserBuilder email(final String email) {
        this.email = email;
        return this;
    }

    public WebsUserBuilder accounts(final Set<Account> accounts) {
        this.accounts = accounts;
        return this;
    }

    public WebsUserBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public WebsUserBuilder loans(final Set<Loan> loans) {
        this.loans = loans;
        return this;
    }

    public WebsUser build() {
        final WebsUser websUser = new WebsUser(username, password, roles, accounts, email);
        websUser.setId(id);
        websUser.setDetails(details);
        websUser.setLoans(loans);
        return websUser;
    }

    public WebsUserBuilder details(final WebsUserDetails userDetails) {
        this.details = userDetails;
        return this;
    }
}
