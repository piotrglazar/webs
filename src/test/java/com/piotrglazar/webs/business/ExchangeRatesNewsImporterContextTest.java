package com.piotrglazar.webs.business;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.model.entities.ExchangeRatesNews;
import com.piotrglazar.webs.model.repositories.WebsNewsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeRatesNewsImporterContextTest extends AbstractContextTest {

    @Autowired
    private ExchangeRatesNewsImporter newsImporter;

    @Autowired
    private WebsNewsRepository websNewsRepository;

    @Test
    public void shouldImportNews() {
        // when
        final Observable<ExchangeRatesNews> exchangeRateNews = newsImporter.fetchNews();

        // then
        final List<ExchangeRatesNews> newsList = exchangeRateNews.toList().toBlocking().first();
        assertThat(newsList).hasSize(1);
        assertThat(newsList.get(0).getBody()).contains("USD", "EUR", "GBP", "PLN");

        // cleanup
        newsList.forEach(websNewsRepository::delete);
    }
}
