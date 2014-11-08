package com.piotrglazar.webs.config;

import com.piotrglazar.webs.util.reactive.AsyncWebsEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

@Configuration
public class ReactiveConfiguration {

    @Bean(name = "asyncWebsEventSubject")
    public Subject<AsyncWebsEvent, AsyncWebsEvent> asyncWebsEventSubject() {
        return PublishSubject.create();
    }

    @Bean(name = "asyncWebsEventObservable")
    public Observable<AsyncWebsEvent> asyncWebsEventObservable(Subject<AsyncWebsEvent, AsyncWebsEvent> asyncWebsEventSubject) {
        return asyncWebsEventSubject.observeOn(Schedulers.io());
    }
}
