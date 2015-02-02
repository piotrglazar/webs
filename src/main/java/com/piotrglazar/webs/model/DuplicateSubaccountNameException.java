package com.piotrglazar.webs.model;

import com.piotrglazar.webs.WebsRuntimeException;

public class DuplicateSubaccountNameException extends WebsRuntimeException {
    public DuplicateSubaccountNameException(final String number, final String name) {
        super(String.format("Account %s has already a subaccount with name %s", number, name));
    }
}
