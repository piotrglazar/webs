package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.dto.NewsDto;
import com.piotrglazar.webs.model.entities.InternalWebsNews;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUiNewsProviderTest {

    @Mock
    private WebsNewsProvider websNewsProvider;

    @InjectMocks
    private DefaultUiNewsProvider uiNewsProvider;

    @Test
    public void shouldConvertNewsToNewsDto() {
        // given
        given(websNewsProvider.getNews()).willReturn(Lists.newArrayList(new InternalWebsNews("head", "body", "url", "urlText", "imgContent")));

        // when
        final Collection<NewsDto> uiNews = uiNewsProvider.getNews();

        // then
        assertThat(uiNews).hasSize(1);
        final NewsDto news = uiNews.iterator().next();
        assertThat(news.getHeadline()).isEqualTo("head");
        assertThat(news.getBody()).isEqualTo("body");
        assertThat(news.getUrl()).isEqualTo("url");
        assertThat(news.getUrlText()).isEqualTo("urlText");
        assertThat(news.getImgContent()).isEqualTo("imgContent");
    }
}
