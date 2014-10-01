package com.piotrglazar.webs.model;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.AbstractContextTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultWebsNewsProviderContextTest extends AbstractContextTest {

    @Autowired
    private DefaultWebsNewsProvider provider;

    @Autowired
    private WebsNewsRepository newsRepository;

    @Test
    public void shouldFetchAllNewsFromRepository() {
        // when
        final Collection<WebsNews> allNews = provider.getNews();

        // then
        assertThat(allNews).hasSize(2);
        final Iterator<WebsNews> newsIterator = allNews.iterator();
        WebsNews news = newsIterator.next();
        assertThat(news.getHeadline()).isEqualTo("Browse your accounts!");
        news = newsIterator.next();
        assertThat(news.getHeadline()).isEqualTo("Create new account!");
    }

    @Test
    public void shouldSaveNews() {
        // given
        final WebsNews news = InternalWebsNews.builder().headline("head").build();

        // when
        provider.addNews(news);

        // then
        assertThat(news.getId()).isNotNull();
        assertThat(newsRepository.findOne(news.getId())).isNotNull();

        // cleanup
        newsRepository.delete(news);
    }

    @Test
    public void shouldDeleteAllNewsOfGivenType() {
        // given
        final TestWebsNews news = new TestWebsNews();
        newsRepository.save(news);

        // when
        provider.removeAll(TestWebsNews.class);

        // then
        assertThat(newsRepository.findOne(news.getId())).isNull();
    }

    @Test
    public void shouldSaveAllNewsAtOnce() {
        // given
        final List<TestWebsNews> news = Lists.newArrayList(new TestWebsNews(), new TestWebsNews());

        // when
        provider.saveAll(news);

        // then
        news.stream().forEach(testNews -> assertThat(newsRepository.findOne(testNews.getId())).isNotNull());

        // cleanup
        news.stream().forEach(newsRepository::delete);
    }
}
