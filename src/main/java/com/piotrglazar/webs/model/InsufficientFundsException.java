package com.piotrglazar.webs.model;

import com.piotrglazar.webs.WebsRuntimeException;

import java.math.BigDecimal;

public class InsufficientFundsException extends WebsRuntimeException {

    public InsufficientFundsException(final String number, final BigDecimal balance) {
        super(String.format("There are insufficient funds on account %s to create subaccount with balance %s", number, balance));
    }

}
