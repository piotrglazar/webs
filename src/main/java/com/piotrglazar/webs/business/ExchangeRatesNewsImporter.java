package com.piotrglazar.webs.business;

import com.piotrglazar.webs.api.ExchangeRateResponse;
import com.piotrglazar.webs.dto.ExchangeRateDto;
import com.piotrglazar.webs.model.entities.ExchangeRatesNews;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import rx.Observable;

import java.net.URI;
import java.util.Set;

@Component
public class ExchangeRatesNewsImporter implements NewsImporter {

    private static final String APP_ID_QUERY_PARAM = "app_id";

    private final Set<String> currencies;
    private final RestTemplate restTemplate;
    private final URI requestUri;
    private final WebsTemplates websTemplates;

    @Autowired
    public ExchangeRatesNewsImporter(@Value("#{businessProperties['exchange.rate.currencies']}") String currencies,
                                     @Value("#{businessProperties['exchange.rate.api']}") String url,
                                     @Value("#{businessProperties['exchange.rate.appId']}") String appId,
                                     RestTemplate restTemplate, WebsTemplates websTemplates) {
        this.websTemplates = websTemplates;
        this.currencies = StringUtils.commaDelimitedListToSet(currencies);
        this.restTemplate = restTemplate;
        this.requestUri = UriComponentsBuilder.fromHttpUrl(url).queryParam(APP_ID_QUERY_PARAM, appId).build().toUri();
    }

    @Override
    public Observable<ExchangeRatesNews> fetchNews() {
        return Observable.create((Observable.OnSubscribe<ExchangeRatesNews>) subscriber -> {
                final ResponseEntity<ExchangeRateResponse> entity = restTemplate.getForEntity(requestUri, ExchangeRateResponse.class);
                final ExchangeRateDto exchangeRates = getExchangeRates(entity.getBody());
                subscriber.onNext(new ExchangeRatesNews("Exchange rates", websTemplates.exchangeRatesNewsBody(exchangeRates)));
                subscriber.onCompleted();
            });
    }

    private ExchangeRateDto getExchangeRates(final ExchangeRateResponse exchangeRateResponse) {
        return ExchangeRateDto.from(exchangeRateResponse, e -> currencies.contains(e.getKey()));
    }

    @Override
    public Class<ExchangeRatesNews> provides() {
        return ExchangeRatesNews.class;
    }

    @Override
    public String name() {
        return "Exchange Rates News";
    }
}
