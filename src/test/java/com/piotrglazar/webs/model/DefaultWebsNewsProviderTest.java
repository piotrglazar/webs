package com.piotrglazar.webs.model;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWebsNewsProviderTest {

    @Mock
    private WebsNewsRepository websNewsRepository;

    @InjectMocks
    private DefaultWebsNewsProvider websNewsProvider;

    @Test
    public void shouldUseRepositoryToFetchAllNews() {
        // given
        final WebsNews websNews = new InternalWebsNews("head", "body", "url", "urlText", "imgContent");
        given(websNewsProvider.getNews()).willReturn(Lists.newArrayList(websNews));

        // when
        final Collection<WebsNews> allNews = websNewsProvider.getNews();

        // then
        assertThat(allNews).hasSize(1);
        assertThat(allNews.iterator().next()).isEqualTo(websNews);
    }

    @Test
    public void shouldUseRepositoryToStoreNews() {
        // given
        final WebsNews websNews = new InternalWebsNews("head", "body", "url", "urlText", "imgContent");

        // when
        websNewsProvider.addNews(websNews);

        // then
        verify(websNewsRepository).saveAndFlush(websNews);
    }

    @Test
    public void shouldUseRepositoryToDeleteAllNews() {
        // when
        websNewsProvider.removeAll(TestWebsNews.class);

        // then
        verify(websNewsRepository).deleteAllNews(TestWebsNews.classSimpleName());
    }

    @Test
    public void shouldUseRepositoryToSaveAllNews() {
        // given
        final List<WebsNews> someNews = Lists.newArrayList(new TestWebsNews());

        // when
        websNewsProvider.saveAll(someNews);

        // then
        verify(websNewsRepository).save(someNews);
    }
}
