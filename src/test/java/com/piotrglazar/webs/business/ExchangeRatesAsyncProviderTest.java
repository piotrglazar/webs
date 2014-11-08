package com.piotrglazar.webs.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrglazar.webs.dto.ExchangeRateDto;
import com.piotrglazar.webs.util.reactive.AsyncWebsEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRatesAsyncProviderTest {

    @Captor
    private ArgumentCaptor<String> json;

    @Mock
    private AsyncWebsEvent asyncWebsEvent;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ExchangeRatesNewsImporter newsImporter;

    @Test
    public void shouldCompleteDeferredResultAfterReceivingEventFromNewsImporter() throws JsonProcessingException {
        // given
        final ExchangeRateDto exchangeRateDto = new ExchangeRateDto("USD", of("PLN", BigDecimal.ONE));
        final Observable<ExchangeRateDto> exchangeRates = Observable.just(exchangeRateDto);
        given(newsImporter.fetchExchangeRates()).willReturn(exchangeRates);
        given(objectMapper.writeValueAsString(exchangeRateDto)).willReturn("USD PLN 1");

        // when
        new ExchangeRatesAsyncProvider(Observable.just(asyncWebsEvent), newsImporter, objectMapper);

        // then
        verify(asyncWebsEvent).complete(json.capture());
        assertThat(json.getValue()).isEqualTo("USD PLN 1");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCompleteDeferredResultWithFailureWhenProcessingJsonFails() throws JsonProcessingException {
        // given
        final ExchangeRateDto exchangeRateDto = new ExchangeRateDto("USD", of("PLN", BigDecimal.ONE));
        final Observable<ExchangeRateDto> exchangeRates = Observable.just(exchangeRateDto);
        given(newsImporter.fetchExchangeRates()).willReturn(exchangeRates);
        given(objectMapper.writeValueAsString(exchangeRateDto)).willThrow(JsonProcessingException.class);

        // when
        new ExchangeRatesAsyncProvider(Observable.just(asyncWebsEvent), newsImporter, objectMapper);

        // then
        verify(asyncWebsEvent).complete(any(JsonProcessingException.class));
    }
}
