package com.piotrglazar.webs.model;

import com.piotrglazar.webs.WebsRuntimeException;

public class WebsAccountNotFoundException extends WebsRuntimeException {
    public WebsAccountNotFoundException(final String accountNumber) {
        super(String.format("Account with number %s not found", accountNumber));
    }

    public WebsAccountNotFoundException(final long accountId) {
        super(String.format("Account with id %d not found", accountId));
    }
}
