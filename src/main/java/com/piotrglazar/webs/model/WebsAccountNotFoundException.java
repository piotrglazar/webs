package com.piotrglazar.webs.model;

public class WebsAccountNotFoundException extends RuntimeException {
    public WebsAccountNotFoundException(final String accountNumber) {
        super(String.format("Account with number %s not found", accountNumber));
    }
}
