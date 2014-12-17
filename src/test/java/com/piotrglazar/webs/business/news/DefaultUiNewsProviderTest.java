package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.business.NewsDtoAssert;
import com.piotrglazar.webs.business.news.DefaultUiNewsProvider;
import com.piotrglazar.webs.dto.NewsDto;
import com.piotrglazar.webs.model.entities.InternalWebsNews;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
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
        given(websNewsProvider.getNews()).willReturn(newArrayList(new InternalWebsNews("head", "body", "url", "urlText", "imgContent")));

        // when
        final Collection<NewsDto> uiNews = uiNewsProvider.getNews();

        // then
        assertThat(uiNews).hasSize(1);
        final NewsDto news = uiNews.iterator().next();
        NewsDtoAssert.assertThat(news)
            .hasHeadline("head")
            .hasBody("body")
            .hasUrl("url")
            .hasUrlText("urlText")
            .hasImgContent("imgContent");
    }
}
