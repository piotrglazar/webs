package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.model.TestWebsNews;
import com.piotrglazar.webs.model.entities.InternalWebsNews;
import com.piotrglazar.webs.model.entities.WebsNews;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
        strategy.saveNews(newsImporter, Observable.from(someNews));

        // then
        InOrder inOrder = inOrder(websNewsProvider);
        inOrder.verify(websNewsProvider).removeAll(eq(TestWebsNews.class));
        inOrder.verify(websNewsProvider).saveAll(someNews);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotRemoveNorSaveWhenThereAreNoNewsOfGivenType() {
        // given
        final NewsImporter newsImporter = mock(NewsImporter.class);
        final List<WebsNews> someNews = Lists.newArrayList();

        // when
        strategy.saveNews(newsImporter, Observable.from(someNews));

        // then
        verify(websNewsProvider, never()).removeAll(any());
        verify(websNewsProvider, never()).saveAll(anyCollection());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotRemoveNorSaveWhenThereAreNoNews() {
        // given
        final Observable<WebsNews> noNews = Observable.empty();

        // when
        strategy.saveAllNews(noNews);

        // then
        verify(websNewsProvider, never()).removeAll(any());
        verify(websNewsProvider, never()).saveAll(anyCollection());
    }

    @Test
    public void shouldRemoveAllNewsBeforeInsertingAllNews() {
        // given
        final List<? extends WebsNews> someNews = Lists.newArrayList(new TestWebsNews(), new InternalWebsNews());

        // when
        strategy.saveAllNews(Observable.from(someNews));

        // then
        InOrder inOrder = Mockito.inOrder(websNewsProvider);
        inOrder.verify(websNewsProvider).removeAll(TestWebsNews.class);
        inOrder.verify(websNewsProvider).removeAll(InternalWebsNews.class);
        inOrder.verify(websNewsProvider).saveAll(someNews);
    }
}
