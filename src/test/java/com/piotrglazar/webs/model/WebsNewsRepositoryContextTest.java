package com.piotrglazar.webs.model;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.model.repositories.WebsNewsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsNewsRepositoryContextTest extends AbstractContextTest {

    @Autowired
    private WebsNewsRepository websNewsRepository;

    @Test
    public void shouldNotFailWhenNoEntitiesToRemove() {
        // expect no fail
        websNewsRepository.deleteAllNews(TestWebsNews.classSimpleName());
    }

    @Test
    public void shouldDeleteAllNewsOfGivenType() {
        // given
        final TestWebsNews testWebsNews = new TestWebsNews();
        websNewsRepository.saveAndFlush(testWebsNews);

        // when
        websNewsRepository.deleteAllNews(TestWebsNews.classSimpleName());

        // then
        assertThat(websNewsRepository.findOne(testWebsNews.getId())).isNull();
    }
}
