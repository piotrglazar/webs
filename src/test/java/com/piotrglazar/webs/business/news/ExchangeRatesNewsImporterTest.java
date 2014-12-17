package com.piotrglazar.webs.business.news;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.webs.api.ExchangeRateResponse;
import com.piotrglazar.webs.dto.ExchangeRateDto;
import com.piotrglazar.webs.model.entities.ExchangeRatesNews;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static com.piotrglazar.webs.TestUtilities.toListToBlocking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRatesNewsImporterTest {

    @Mock
    private WebsTemplates websTemplates;

    @Mock
    private RestTemplate restTemplate;

    private ExchangeRatesNewsImporter newsImporter;

    @Before
    public void setUp() throws Exception {
        newsImporter = new ExchangeRatesNewsImporter("PLN", "http://url.com", "appId", restTemplate, websTemplates);
    }

    @Test
    public void shouldFetchNews() {
        // given
        given(restTemplate.getForEntity(any(URI.class), eq(ExchangeRateResponse.class))).willReturn(responseEntity(exchangeRateResponse()));
        given(websTemplates.exchangeRatesNewsBody(any(ExchangeRateDto.class))).willReturn("PLN / USD: 10");

        // when
        final Observable<ExchangeRatesNews> news = newsImporter.fetchNews();

        // then
        final List<ExchangeRatesNews> newsList = toListToBlocking(news);
        assertThat(newsList).hasSize(1);
        assertThat(newsList.get(0).getHeadline()).isEqualTo("Exchange rates");
        assertThat(newsList.get(0).getBody()).contains("PLN / USD: 10");
    }

    @Test
    public void shouldProvideExchangeRatesNews() {
        // expect
        assertThat(newsImporter.provides()).isEqualTo(ExchangeRatesNews.class);
    }

    @Test
    public void shouldFetchExchangeRates() {
        // given
        given(restTemplate.getForEntity(any(URI.class), eq(ExchangeRateResponse.class))).willReturn(responseEntity(exchangeRateResponse()));
        given(websTemplates.exchangeRatesNewsBody(any(ExchangeRateDto.class))).willReturn("PLN / USD: 10");

        // when
        final Observable<ExchangeRateDto> exchangeRates = newsImporter.fetchExchangeRates();

        // then
        final List<ExchangeRateDto> exchangeRatesList = toListToBlocking(exchangeRates);
        assertThat(exchangeRatesList).hasSize(1);
        assertThat(exchangeRatesList.get(0).getBase()).isEqualTo("USD");
        assertThat(exchangeRatesList.get(0).getRates())
                .containsEntry("PLN", BigDecimal.TEN)
                .hasSize(1);
    }

    private ResponseEntity<ExchangeRateResponse> responseEntity(final ExchangeRateResponse exchangeRateResponse) {
        return new ResponseEntity<>(exchangeRateResponse, HttpStatus.OK);
    }

    private ExchangeRateResponse exchangeRateResponse() {
        final ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();

        exchangeRateResponse.setBase("USD");
        exchangeRateResponse.setRates(ImmutableMap.of("PLN", BigDecimal.TEN));

        return exchangeRateResponse;
    }
}
