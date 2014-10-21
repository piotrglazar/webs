package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.model.TestWebsNews;
import com.piotrglazar.webs.model.entities.WebsNews;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class DefaultNewsImportersTest {

    @Captor
    private ArgumentCaptor<Observable<? extends WebsNews>> allNewsObservable;

    private NewsImporter firstNewsImporter;

    private NewsImporter secondNewsImporter;

    @Mock
    private NewsImportingStrategy newsImportingStrategy;

    private DefaultNewsImporters defaultNewsImporters;

    @Before
    public void setUp() throws Exception {
        firstNewsImporter = mock(NewsImporter.class);
        secondNewsImporter = mock(NewsImporter.class);
        List<NewsImporter> newsImporters = Lists.newArrayList(firstNewsImporter, secondNewsImporter);

        defaultNewsImporters = new DefaultNewsImporters(newsImporters, newsImportingStrategy);
    }

    @Test
    public void shouldFetchNewsFromAllNewsImporters() {
        // given
        firstNewsImporter(new TestWebsNews(), new TestWebsNews());
        secondNewsImporter(new TestWebsNews(), new TestWebsNews());

        // when
        defaultNewsImporters.fetchAllNews();

        // then
        assertThatAllNewsAreFetched();
    }

    private void assertThatAllNewsAreFetched() {
        verify(newsImportingStrategy).saveAllNews(allNewsObservable.capture());
        final List<? extends WebsNews> allNews = allNewsObservable.getValue().toList().toBlocking().first();
        assertThat(allNews).hasSize(4);
    }

    private void secondNewsImporter(final TestWebsNews... testWebsNews) {
        Observable<? extends WebsNews> secondNews = Observable.from(testWebsNews);
        given(secondNewsImporter.fetchNews()).willReturn((Observable) secondNews);
    }

    private void firstNewsImporter(final TestWebsNews... testWebsNews) {
        Observable<? extends WebsNews> firstNews = Observable.from(testWebsNews);
        given(firstNewsImporter.fetchNews()).willReturn((Observable) firstNews);
    }
}
