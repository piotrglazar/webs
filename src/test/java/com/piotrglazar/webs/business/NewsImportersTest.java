package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.model.TestWebsNews;
import com.piotrglazar.webs.model.WebsNews;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
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
        newsImporters = new NewsImporters(Lists.newArrayList(firstNewsImporter, secondNewsImporter), newsImportingStrategy);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldImportNewsUsingStrategy() {
        // given
        final List<? extends WebsNews> news = Lists.newArrayList(new TestWebsNews());
        given(firstNewsImporter.fetchNews()).willReturn((List) news);

        // when
        newsImporters.fetchNews(0);

        // then
        verify(firstNewsImporter).fetchNews();
        verify(secondNewsImporter, never()).fetchNews();
        verify(newsImportingStrategy).saveNews(firstNewsImporter, news);
    }

    @Test(expected = NoSuchNewsImporterException.class)
    public void shouldFailWhenNegativeNewsImporterIndexProvided() {
        // expected
        newsImporters.fetchNews(-1);
    }

    @Test(expected = NoSuchNewsImporterException.class)
    public void shouldFailWhenTooBigNewsImporterIndexProvided() {
        // when
        newsImporters.fetchNews(3);
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
