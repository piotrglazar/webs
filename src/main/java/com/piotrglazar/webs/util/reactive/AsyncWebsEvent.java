package com.piotrglazar.webs.util.reactive;

import org.springframework.web.context.request.async.DeferredResult;

public class AsyncWebsEvent {

    private final DeferredResult<String> deferredResult;

    public AsyncWebsEvent(final DeferredResult<String> deferredResult) {
        this.deferredResult = deferredResult;
    }

    public void complete(String result) {
        deferredResult.setResult(result);
    }

    public void complete(Throwable throwable) {
        deferredResult.setErrorResult(throwable);
    }
}
