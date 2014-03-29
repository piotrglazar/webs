package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;

import java.util.Set;

public class WebsUserBuilder {
    private String username;
    private String password;
    private String email;
    private Set<String> roles = Sets.newHashSet("USER");
    private Set<Account> accounts = Sets.newHashSet();

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

    public WebsUser build() {
        return new WebsUser(username, password, roles, accounts, email);
    }
}