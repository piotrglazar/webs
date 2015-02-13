package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.dto.BloombergNewsBody;
import com.piotrglazar.webs.dto.BloombergNewsBodyFactory;
import com.piotrglazar.webs.model.entities.BloombergNews;
import com.piotrglazar.webs.util.readers.WebsiteReader;
import com.piotrglazar.webs.util.readers.WebsiteReaderFactory;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static com.piotrglazar.webs.TestUtilities.toListToBlocking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;

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

        // when
        final List<BloombergNewsBody> news = bloombergNewsImporter.bloombergNewsBodies(pageContent);

        // then
        assertThatWebsNewsBodyContains(news);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateBloombergNewsFromBloombergPage() {
        // given
        given(websTemplates.bloombergNewsBody(anyList())).willReturn("bloombergNewsBody");

        // when
        final Observable<BloombergNews> news = bloombergNewsImporter.fetchNews();

        // then
        final List<BloombergNews> newsList = toListToBlocking(news);
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
        assertThat(bloombergNewsBody.get(0)).isEqualTo(new BloombergNewsBody("DJIA", "+110.24", true, "17,972.38", "+0.62"));
        assertThat(bloombergNewsBody.get(1)).isEqualTo(new BloombergNewsBody("S&P 500", "+19.95", true, "2,088.48", "+0.96"));
        assertThat(bloombergNewsBody.get(2)).isEqualTo(new BloombergNewsBody("FTSE 100", "+9.94", true, "6,828.11", "+0.15"));
        assertThat(bloombergNewsBody.get(3)).isEqualTo(new BloombergNewsBody("Nikkei 225", "+327.04", true, "17,979.72", "+1.85"));
        assertThat(bloombergNewsBody.get(4)).isEqualTo(new BloombergNewsBody("Crude Oil (WTI)", "-2.29", false, "51.13", "-4.69"));
    }
}
