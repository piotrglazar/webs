package com.piotrglazar.webs.model;

import com.piotrglazar.webs.WebsRuntimeException;

public class WebsUserNotFoundException extends WebsRuntimeException {
    public WebsUserNotFoundException(final String username) {
        super(String.format("There is no user %s", username));
    }
}
