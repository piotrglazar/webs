package com.piotrglazar.webs.util;

import com.piotrglazar.webs.NewsImporters;
import com.piotrglazar.webs.util.beans.NewsImportingBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NewsImportingBeanTest {

    @Mock
    private NewsImporters newsImporters;

    @InjectMocks
    private NewsImportingBean newsImportingBean;

    @Test
    public void shouldFetchAllNews() {
        // when
        newsImportingBean.importNews();

        // then
        verify(newsImporters).fetchAllNews();
    }

    @Test
    public void shouldParticularFetchNews() {
        // given
        final int newsImporterIndex = 1;

        // when
        newsImportingBean.importNews(newsImporterIndex);

        // then
        verify(newsImporters).fetchNews(newsImporterIndex);
    }
}
