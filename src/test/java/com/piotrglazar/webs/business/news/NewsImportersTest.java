package com.piotrglazar.webs.business.news;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.NewsImporters;
import com.piotrglazar.webs.business.NewsImporter;
import com.piotrglazar.webs.business.NewsImportingStrategy;
import com.piotrglazar.webs.model.TestWebsNews;
import com.piotrglazar.webs.model.entities.WebsNews;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class NewsImportersTest {

    @Mock
    private NewsImporter firstNewsImporter;

    @Mock
    private NewsImporter secondNewsImporter;

    @Mock
    private NewsImportingStrategy newsImportingStrategy;

    private NewsImporters newsImporters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        newsImporters = new DefaultNewsImporters(Lists.newArrayList(firstNewsImporter, secondNewsImporter), newsImportingStrategy);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldImportNewsUsingStrategy() {
        // given
        final Observable<? extends WebsNews> news = Observable.from(Lists.newArrayList(new TestWebsNews()));
        given(firstNewsImporter.fetchNews()).willReturn((Observable) news);

        // when
        newsImporters.fetchNews(0);

        // then
        verify(firstNewsImporter).fetchNews();
        verify(secondNewsImporter, never()).fetchNews();
        verify(newsImportingStrategy).saveNews(firstNewsImporter, news);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    @Parameters({
            "-1",
            "3"
    })
    public void shouldFailWhenInvalidNewsImporterIndexProvided(int i) {
        // expected
        newsImporters.fetchNews(i);
    }

    @Test
    public void shouldListNewsImporters() {
        // given
        given(firstNewsImporter.name()).willReturn("first");
        given(secondNewsImporter.name()).willReturn("second");

        // when
        final List<String> newsImportersNames = newsImporters.getNewsImportersNames();

        // then
        assertThat(newsImportersNames).containsExactly("first", "second");
    }
}
