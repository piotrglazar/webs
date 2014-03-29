package com.piotrglazar.webs.business;

import com.piotrglazar.webs.WebsRuntimeException;

public class MoneyTransferException extends WebsRuntimeException {

    public MoneyTransferException(final String message) {
        super(message);
    }
}
