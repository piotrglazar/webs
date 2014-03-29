package com.piotrglazar.webs;

public class WebsRuntimeException extends RuntimeException {

    public WebsRuntimeException() {

    }

    public WebsRuntimeException(final String message) {
        super(message);
    }

    public WebsRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
