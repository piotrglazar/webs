package com.piotrglazar.webs.business;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.model.ExchangeRatesNews;
import com.piotrglazar.webs.model.WebsNewsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        final List<ExchangeRatesNews> exchangeRateNews = newsImporter.fetchNews();

        // then
        assertThat(exchangeRateNews).hasSize(1);
        assertThat(exchangeRateNews.get(0).getBody()).contains("USD", "EUR", "GBP", "PLN");

        // cleanup
        exchangeRateNews.stream().forEach(websNewsRepository::delete);
    }
}
