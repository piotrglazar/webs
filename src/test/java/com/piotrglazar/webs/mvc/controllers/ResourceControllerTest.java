package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.util.reactive.AsyncWebsEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.request.async.DeferredResult;
import rx.subjects.Subject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResourceControllerTest {

    @Captor
    ArgumentCaptor<AsyncWebsEvent> captor;

    @Mock
    private Subject<AsyncWebsEvent, AsyncWebsEvent> subject;

    @InjectMocks
    private ResourceController resourceController;

    @Test
    public void shouldStartAsyncComputationWhenRequestArrives() {
        // given
        final DeferredResult<String> exchangeRates = resourceController.getExchangeRates();

        // when
        verify(subject).onNext(captor.capture());
        captor.getValue().complete("exchangeRate");

        // then
        assertThat(exchangeRates.getResult()).isEqualTo("exchangeRate");
    }
}
