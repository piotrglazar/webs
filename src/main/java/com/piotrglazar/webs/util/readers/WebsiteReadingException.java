package com.piotrglazar.webs.util.readers;

import com.piotrglazar.webs.WebsRuntimeException;

import java.net.URI;

public class WebsiteReadingException extends WebsRuntimeException {
    public WebsiteReadingException(final URI uri) {
        super(uri.toString());
    }
}
