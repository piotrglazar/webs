package com.piotrglazar.webs.business.exceptions;

import com.google.common.base.Joiner;
import com.piotrglazar.webs.WebsRuntimeException;
import com.piotrglazar.webs.util.ErrorGatherer;

import java.util.List;

public class MoneyTransferException extends WebsRuntimeException {

    public MoneyTransferException(final List<String> message) {
        super(getExceptionMessage(message));
    }

    public MoneyTransferException(final ErrorGatherer errorGatherer) {
        this(errorGatherer.getErrorMessages());
    }

    private static String getExceptionMessage(final List<String> message) {
        return Joiner.on('\n').join(message);
    }
}
