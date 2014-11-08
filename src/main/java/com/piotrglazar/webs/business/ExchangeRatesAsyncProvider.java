package com.piotrglazar.webs.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrglazar.webs.util.reactive.AsyncWebsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;

import java.lang.invoke.MethodHandles;

@Component
public class ExchangeRatesAsyncProvider extends Subscriber<AsyncWebsEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ExchangeRatesNewsImporter exchangeRatesNewsImporter;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExchangeRatesAsyncProvider(@Qualifier("asyncWebsEventObservable") Observable<AsyncWebsEvent> observable,
                                      ExchangeRatesNewsImporter exchangeRatesNewsImporter, ObjectMapper objectMapper) {
        this.exchangeRatesNewsImporter = exchangeRatesNewsImporter;
        this.objectMapper = objectMapper;
        observable.subscribe(this);
    }

    @Override
    public void onCompleted() {
        LOG.info("Completed providing exchange rates");
    }

    @Override
    public void onError(final Throwable e) {
        LOG.error("Failed because of ", e);
    }

    @Override
    public void onNext(final AsyncWebsEvent asyncWebsEvent) {
        exchangeRatesNewsImporter.fetchExchangeRates()
                .subscribe(exchangeRates -> {
                    try {
                        asyncWebsEvent.complete(objectMapper.writeValueAsString(exchangeRates));
                    } catch (JsonProcessingException e) {
                        asyncWebsEvent.complete(e);
                    }
                });
    }
}
