package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.util.reactive.AsyncWebsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import rx.subjects.Subject;

import java.lang.invoke.MethodHandles;

@Controller
public class ResourceController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Subject<AsyncWebsEvent, AsyncWebsEvent> subject;

    @Autowired
    public ResourceController(@Qualifier("asyncWebsEventSubject") Subject<AsyncWebsEvent, AsyncWebsEvent> subject) {
        this.subject = subject;
    }

    @RequestMapping("/resources/exchangeRates")
    @ResponseBody
    public DeferredResult<String> getExchangeRates() {
        LOG.info("Request served by {}", Thread.currentThread().getName());
        final DeferredResult<String> result = new DeferredResult<>(null, "default");
        subject.onNext(new AsyncWebsEvent(result));
        return result;
    }
}
