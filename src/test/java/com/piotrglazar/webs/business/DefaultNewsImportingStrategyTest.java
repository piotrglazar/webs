package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.model.TestWebsNews;
import com.piotrglazar.webs.model.entities.WebsNews;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultNewsImportingStrategyTest {

    @Mock
    private WebsNewsProvider websNewsProvider;

    @InjectMocks
    private DefaultNewsImportingStrategy strategy;

    @Test
    public void shouldRemoveAllNewsOfGivenTypeBeforeInsertingNewOnes() {
        // given
        final NewsImporter newsImporter = mock(NewsImporter.class);
        final List<WebsNews> someNews = Lists.newArrayList(new TestWebsNews());
        given(newsImporter.provides()).willAnswer(o -> TestWebsNews.class);

        // when
        strategy.saveNews(newsImporter, someNews);

        // then
        InOrder inOrder = inOrder(websNewsProvider);
        inOrder.verify(websNewsProvider).removeAll(eq(TestWebsNews.class));
        inOrder.verify(websNewsProvider).saveAll(someNews);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenNoNewsToSave() {
        // given
        final NewsImporter newsImporter = mock(NewsImporter.class);
        final List<WebsNews> someNews = Lists.newArrayList();

        // when
        strategy.saveNews(newsImporter, someNews);
    }
}
