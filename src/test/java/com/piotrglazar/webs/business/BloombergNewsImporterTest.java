package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.BloombergNews;
import com.piotrglazar.webs.util.WebsiteReader;
import com.piotrglazar.webs.util.WebsiteReaderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class BloombergNewsImporterTest {

    @Mock
    private WebsiteReaderFactory websiteReaderFactory;

    @Mock
    private WebsiteReader websiteReader;

    private BloombergNewsImporter bloombergNewsImporter;

    private String webPage() throws URISyntaxException, IOException {
        Stream<String> lines = Files.lines(Paths.get(getClass().getClassLoader().getResource("newsimporters/bloomberg.html").toURI()));
        return lines.collect(Collectors.joining());
    }

    @Before
    public void setUp() throws IOException, URISyntaxException {
        given(websiteReaderFactory.websiteReader(anyString())).willReturn(websiteReader);
        given(websiteReader.get()).willReturn(webPage());

        bloombergNewsImporter = new BloombergNewsImporter(websiteReaderFactory);
    }

    @Test
    public void shouldFetchNewsFromBloombergPage() throws URISyntaxException, IOException {
        // given
        final String pageContent = webPage();

        // when
        final String tickers = bloombergNewsImporter.extractTickers(pageContent);
        final String[] rawTickers = tickers.split("\n");

        // then
        assertThat(rawTickers).containsExactly("DJIA +29.41 +0.18% 16,810.42",
                "S&P 500 +5.77 +0.30% 1,943.55",
                "FTSE 100 +12.13 +0.18% 6,766.77",
                "Nikkei 225 +43 +0.29% 14,976",
                "Crude Oil (WTI) -0.71 -0.66% 106.19");
    }

    @Test
    public void shouldCreateBloomberNewsFromBloombergPage() {
        // when
        final List<BloombergNews> news = bloombergNewsImporter.fetchNews();

        // then
        assertThat(news).hasSize(1);
        final BloombergNews bloombergNews = news.get(0);
        assertThat(bloombergNews.getHeadline()).isEqualTo("Latest news from Bloomberg");
        assertThat(bloombergNews.getUrl()).isEqualTo(BloombergNewsImporter.BLOOMBERG);
        assertThat(bloombergNews.getBody()).contains("DJIA +29.41 +0.18% 16,810.42",
                "S&P 500 +5.77 +0.30% 1,943.55",
                "FTSE 100 +12.13 +0.18% 6,766.77",
                "Nikkei 225 +43 +0.29% 14,976",
                "Crude Oil (WTI) -0.71 -0.66% 106.19");
    }
}
