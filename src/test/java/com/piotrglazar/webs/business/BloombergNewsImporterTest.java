package com.piotrglazar.webs.business;

import com.piotrglazar.webs.dto.BloombergNewsBody;
import com.piotrglazar.webs.dto.BloombergNewsBodyFactory;
import com.piotrglazar.webs.model.entities.BloombergNews;
import com.piotrglazar.webs.util.readers.WebsiteReader;
import com.piotrglazar.webs.util.readers.WebsiteReaderFactory;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BloombergNewsImporterTest {

    @Mock
    private WebsTemplates websTemplates;

    private BloombergNewsBodyFactory bloombergNewsBodyFactory = new BloombergNewsBodyFactory();

    @Mock
    private WebsiteReaderFactory websiteReaderFactory;

    @Mock
    private WebsiteReader websiteReader;

    private BloombergNewsImporter bloombergNewsImporter;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        given(websiteReaderFactory.websiteReader(anyString())).willReturn(websiteReader);
        given(websiteReader.get()).willReturn(webPage());

        bloombergNewsImporter = new BloombergNewsImporter(websiteReaderFactory, bloombergNewsBodyFactory, websTemplates);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFetchNewsFromBloombergPage() throws URISyntaxException, IOException {
        // given
        final String pageContent = webPage();
        given(websTemplates.bloombergNewsBody(anyList())).willReturn("bloombergNewsBody");

        // when
        final String tickers = bloombergNewsImporter.extractTickers(pageContent);

        // then
        assertThat(tickers).isEqualTo("bloombergNewsBody");
        final ArgumentCaptor<List> bloombergNewsBody = ArgumentCaptor.forClass(List.class);
        verify(websTemplates).bloombergNewsBody(bloombergNewsBody.capture());
        assertThatWebsNewsBodyContains(bloombergNewsBody.getValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateBloombergNewsFromBloombergPage() {
        // given
        given(websTemplates.bloombergNewsBody(anyList())).willReturn("bloombergNewsBody");

        // when
        final Observable<BloombergNews> news = bloombergNewsImporter.fetchNews();

        // then
        final List<BloombergNews> newsList = news.toList().toBlocking().first();
        assertThat(newsList).hasSize(1);
        final BloombergNews bloombergNews = newsList.get(0);
        assertThat(bloombergNews.getHeadline()).isEqualTo("Latest news from Bloomberg");
        assertThat(bloombergNews.getUrl()).isEqualTo(BloombergNewsImporter.BLOOMBERG);
        assertThat(bloombergNews.getBody()).contains("bloombergNewsBody");
    }

    @Test
    public void shouldProvideBloombergNews() {
        // expect
        assertThat(bloombergNewsImporter.provides()).isEqualTo(BloombergNews.class);
    }

    private String webPage() throws URISyntaxException, IOException {
        Stream<String> lines = Files.lines(Paths.get(getClass().getClassLoader().getResource("newsimporters/bloomberg.html").toURI()));
        return lines.collect(Collectors.joining());
    }

    private void assertThatWebsNewsBodyContains(final List<BloombergNewsBody> bloombergNewsBody) {
        assertThat(bloombergNewsBody).hasSize(5);
        assertThat(bloombergNewsBody.get(0)).isEqualTo(new BloombergNewsBody("DJIA", "+29.41 +0.18%", true, "16,810.42"));
        assertThat(bloombergNewsBody.get(1)).isEqualTo(new BloombergNewsBody("S&P 500", "+5.77 +0.30%", true, "1,943.55"));
        assertThat(bloombergNewsBody.get(2)).isEqualTo(new BloombergNewsBody("FTSE 100", "+12.13 +0.18%", true, "6,766.77"));
        assertThat(bloombergNewsBody.get(3)).isEqualTo(new BloombergNewsBody("Nikkei 225", "+43 +0.29%", true, "14,976"));
        assertThat(bloombergNewsBody.get(4)).isEqualTo(new BloombergNewsBody("Crude Oil (WTI)", "-0.71 -0.66%", false, "106.19"));
    }
}
