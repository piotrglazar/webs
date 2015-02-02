package com.piotrglazar.webs.model;

import com.piotrglazar.webs.WebsRuntimeException;

public class WebsSubaccountNotFoundException extends WebsRuntimeException {
    public WebsSubaccountNotFoundException(String number, String subaccountName) {
        super(String.format("Account %s has no subaccount %s", number, subaccountName));
    }
}
